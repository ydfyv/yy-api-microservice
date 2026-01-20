package com.yy.yyapibackend.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yy.yyapibackend.common.ErrorCode;
import com.yy.yyapibackend.constant.CommonConstant;
import com.yy.yyapibackend.exception.BusinessException;
import com.yy.yyapibackend.exception.ThrowUtils;
import com.yy.yyapibackend.mapper.InterfaceInfoMapper;
import com.yy.yyapibackend.model.dto.interfaceInfo.InterfaceInfoQueryRequest;
import com.yy.yyapibackend.model.vo.InterfaceInfoVO;
import com.yy.yyapibackend.model.vo.UserVO;
import com.yy.yyapibackend.service.InterfaceInfoService;
import com.yy.yyapibackend.service.UserService;
import com.yy.yyapibackend.utils.SqlUtils;
import com.yy.yyapiclientsdk.client.YyApiClient;
import com.yy.yyapimodel.model.entity.InterfaceInfo;
import com.yy.yyapimodel.model.entity.User;
import com.yy.yyapimodel.model.enums.HttpMethodEnum;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static com.yy.yyapibackend.constant.CommonConstant.BASE_PATH;
import static com.yy.yyapibackend.constant.CommonConstant.GATEWAY_URL;

/**
 * @author 阿狸
 */
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo> implements InterfaceInfoService {

    @Resource
    @Lazy
    private UserService userService;

    @Resource
    private YyApiClient yyApiClient;

    private static final Map<String, Object> X_OPENAPI = new HashMap<>();

    static {
        Map<String, Object> xSetting = new HashMap<>();
        xSetting.put("language", "zh-CN");
        xSetting.put("enableSwaggerModels", true);
        xSetting.put("swaggerModelName", "Swagger Models");
        xSetting.put("enableReloadCacheParameter", false);
        xSetting.put("enableAfterScript", true);
        xSetting.put("enableDocumentManage", true);
        xSetting.put("enableVersion", false);
        xSetting.put("enableRequestCache", true);
        xSetting.put("enableFilterMultipartApis", false);
        xSetting.put("enableFilterMultipartApiMethodType", "POST");
        xSetting.put("enableHost", false);
        xSetting.put("enableHostText", "");
        xSetting.put("enableDynamicParameter", false);
        xSetting.put("enableDebug", true);
        xSetting.put("enableFooter", true);
        xSetting.put("enableFooterCustom", false);
        xSetting.put("footerCustomContent", null);
        xSetting.put("enableSearch", true);
        xSetting.put("enableOpenApi", true);
        xSetting.put("enableHomeCustom", false);
        xSetting.put("homeCustomLocation", null);
        xSetting.put("enableGroup", true);
        xSetting.put("enableResponseCode", true);

        X_OPENAPI.put("x-markdownFiles", null);
        X_OPENAPI.put("x-setting", xSetting);
    }

    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo) {

        String path = interfaceInfo.getPath();
        String requestParams = interfaceInfo.getRequestParams();
        String responseParams = interfaceInfo.getResponseParams();
        String method = interfaceInfo.getMethod();

        ThrowUtils.throwIf(StringUtils.isBlank(path), ErrorCode.PARAMS_ERROR, "路径参数为空");
        ThrowUtils.throwIf(StringUtils.isBlank(String.valueOf(method)), ErrorCode.PARAMS_ERROR, "请求方式参数为空");
        ThrowUtils.throwIf(StringUtils.isBlank(requestParams), ErrorCode.PARAMS_ERROR, "请求参数为空");
        ThrowUtils.throwIf(StringUtils.isBlank(responseParams), ErrorCode.PARAMS_ERROR, "响应参数为空");

        String regex = "^https?://[\\w\\-.]+(:\\d+)?(/[\\w\\-._~:/?#\\[\\]@!$&'()*+,;=]*)?$";

//        ThrowUtils.throwIf(!regex.matches(url), ErrorCode.PARAMS_ERROR, "url格式不正确");

        ThrowUtils.throwIf(HttpMethodEnum.getEnumByValue(method) == null, ErrorCode.PARAMS_ERROR, "method错误！");
    }


    /**
     * 获取查询包装类
     *
     * @param interfaceInfoQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<InterfaceInfo> getQueryWrapper(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        if (interfaceInfoQueryRequest == null) {
            return queryWrapper;
        }
        Long id = interfaceInfoQueryRequest.getId();
        String name = interfaceInfoQueryRequest.getName();
        String description = interfaceInfoQueryRequest.getDescription();
        String serverUri = interfaceInfoQueryRequest.getServerUri();
        String status = interfaceInfoQueryRequest.getStatus();
        String method = interfaceInfoQueryRequest.getMethod();
        Long userId = interfaceInfoQueryRequest.getUserId();
        String sortField = interfaceInfoQueryRequest.getSortField();
        String sortOrder = interfaceInfoQueryRequest.getSortOrder();

        // 拼接查询条件
        queryWrapper.like(ObjectUtils.isNotEmpty(name), "name", name);
        queryWrapper.like(ObjectUtils.isNotEmpty(description), "description", description);
        queryWrapper.like(ObjectUtils.isNotEmpty(serverUri), "serverUri", serverUri);
        queryWrapper.eq(ObjectUtils.isNotEmpty(status), "status", status);
        queryWrapper.eq(ObjectUtils.isNotEmpty(method), "method", method);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        return queryWrapper;
    }


    @Override
    public InterfaceInfoVO getInterfaceInfoVO(InterfaceInfo interfaceInfo, HttpServletRequest request) {
        InterfaceInfoVO interfaceInfoVO = InterfaceInfoVO.objToVo(interfaceInfo);

        Long userId = interfaceInfo.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        interfaceInfoVO.setUser(userVO);

        return interfaceInfoVO;
    }

    @Override
    public Page<InterfaceInfoVO> getInterfaceInfoVOPage(Page<InterfaceInfo> interfaceInfoPage, HttpServletRequest request) {
        List<InterfaceInfo> interfaceInfoList = interfaceInfoPage.getRecords();
        Page<InterfaceInfoVO> interfaceInfoVOPage = new Page<>(interfaceInfoPage.getCurrent(), interfaceInfoPage.getSize(), interfaceInfoPage.getTotal());
        if (CollUtil.isEmpty(interfaceInfoList)) {
            return interfaceInfoVOPage;
        }

        Set<Long> userIdSet = interfaceInfoList.stream().map(InterfaceInfo::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream().collect(Collectors.groupingBy(User::getId));

        // 填充信息
        List<InterfaceInfoVO> interfaceInfoVOList = interfaceInfoList.stream().map(interfaceInfo -> {
            InterfaceInfoVO interfaceInfoVO = InterfaceInfoVO.objToVo(interfaceInfo);
            Long userId = interfaceInfo.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            interfaceInfoVO.setUser(userService.getUserVO(user));
            return interfaceInfoVO;
        }).collect(Collectors.toList());
        interfaceInfoVOPage.setRecords(interfaceInfoVOList);
        return interfaceInfoVOPage;
    }

    @Override
    public void validateAccessible(InterfaceInfo interfaceInfo) {
        // TODO: 根据数据库中的url、method、requestHeader、responseHeader等信息，进行访问校验
//        String url = interfaceInfo.getUrl();
//        String requestHeader = interfaceInfo.getRequestHeader();
//        String responseHeader = interfaceInfo.getResponseHeader();
//        String status = interfaceInfo.getStatus();
//        String method = interfaceInfo.getMethod();

//        com.yy.yyapiclientsdk.model.User user = new com.yy.yyapiclientsdk.model.User();
//        user.setName("yy");
//
//        String response = yyApiClient.getNameByPost(user);
//
//        System.out.println("response: " + response);

    }

    @Override
    public Map<String, Object> getOpenApiDoc(String path) {
        InterfaceInfo interfaceInfo = lambdaQuery().eq(InterfaceInfo::getPath, path).eq(InterfaceInfo::getStatus, 1).one();

        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口不存在！");
        }

        return buildDoc(interfaceInfo);
    }


// region Swagger文档生成 只支持POST请求
//    private Map<String, Object> buildDoc(InterfaceInfo interfaceInfo) {
//
//        String path = interfaceInfo.getPath();
//
//        Map<String, Object> map = new HashMap<>();
//        map.put("swagger", "2.0");
//        map.put("info", Collections.singletonMap("title", "接口文档"));
//        map.put("host", GATEWAY_URL);
//        map.put("schemes", Collections.singletonList("http"));
//        map.put("basePath", BASE_PATH);
//        map.put("tags", Collections.singletonList(Collections.singletonMap("name", path)));
//        map.put("paths", buildPath(interfaceInfo));
//        map.put("definitions", buildDefinitions(interfaceInfo));
//        map.put("x-openapi", X_OPENAPI);
//        return map;
//    }
//
//
//    private Map<String, Object> buildPath(InterfaceInfo interfaceInfo) {
//        String name = interfaceInfo.getName();
//        String description = interfaceInfo.getDescription();
//        String methodName = interfaceInfo.getMethodName();
//        String path = interfaceInfo.getPath();
//        String method = interfaceInfo.getMethod();
//        String transPattern = interfaceInfo.getTransPattern();
//        String requestParams = interfaceInfo.getRequestParams();
//
//
//        List<Map<String, Object>> parameters = new ArrayList<>();
//
//        if ("POST".equals(method)) {
//            Map<String, Object> map = new HashMap<>();
//            map.put("in", "body");
//            map.put("name", methodName);
//            map.put("description", description);
//            map.put("required", true);
//            map.put("schema", Collections.singletonMap("$ref", "#/definitions/" + methodName));
//            parameters.add(map);
//        } else if ("GET".equals(method)) {
//            Map<String, Object> params;
//            try {
//                params = JSON.parseObject(requestParams, Map.class);
//            } catch (Exception e) {
//                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据库requestParams格式错误！");
//            }
//            // 参数名：参数类型  例： name: string
//            for (Map.Entry<String, Object> entry : params.entrySet()) {
//                Map<String, Object> map = new HashMap<>();
//                map.put("in", "query");
//                map.put("name", entry.getKey());
//                map.put("type", entry.getValue());
//                parameters.add(map);
//            }
//        }
//
//        Map<String, Object> methodMap = new HashMap<>();
//        methodMap.put("tags", Collections.singletonList(name));
//        methodMap.put("summary", "check");
//        methodMap.put("operationId", methodName);
//        methodMap.put("products", Collections.singletonList("*/*"));
//        methodMap.put("parameters", parameters);
//
//        Map<String, HashMap<String, Object>> responses = new HashMap<>();
//
//        HashMap<String, Object> map200 = new HashMap<>();
//        map200.put("description", "OK");
//        map200.put("schema", Collections.singletonMap("type", "string"));
//
//        HashMap<String, Object> map401 = new HashMap<>();
//        map401.put("description", "Unauthorized");
//
//        HashMap<String, Object> map403 = new HashMap<>();
//        map403.put("description", "Forbidden");
//
//        HashMap<String, Object> map404 = new HashMap<>();
//        map404.put("description", "Not Found");
//
//        responses.put("200", map200);
//        responses.put("401", map401);
//        responses.put("403", map403);
//        responses.put("404", map404);
//
//
//        methodMap.put("responses", responses);
//        methodMap.put("responsesObject", responses);
//        methodMap.put("deprecated", false);
//
//
//        Map<String, Object> pathMap = new HashMap<>();
//
//
//        Map<String, Object> httpMethodMap = new HashMap<>();
//        HttpMethodEnum methodEnum = HttpMethodEnum.getEnumByValue(method);
//        if (methodEnum == null) {
//            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Http请求方式不存在！");
//        }
//        httpMethodMap.put(methodEnum.getMethod(), methodMap);
//
//        pathMap.put(transPattern + path, httpMethodMap);
//
//        return pathMap;
//    }
//
//
//    private Map<String, Object> buildDefinitions(InterfaceInfo interfaceInfo) {
//        String methodName = interfaceInfo.getMethodName();
//        String requestParams = interfaceInfo.getRequestParams();
//        String responseParams = interfaceInfo.getResponseParams();
//
//        Map<String, Object> map = new HashMap<>();
//
//        Map<String, Object> baseResponse = new HashMap<>();
//
//        HashMap<String, Object> properties = new HashMap<>();
//
//        HashMap<String, Object> code = new HashMap<>();
//        code.put("type", "integer");
//        code.put("format", "int32");
//
//        // 需取定义一个响应对象
//        Map<String, Object> responseMap = new HashMap<>();
//        responseMap.put("type", "object");
//        responseMap.put("properties", JSON.parseObject(responseParams, Map.class));
//        responseMap.put("title", methodName + "_Response");
//
//        HashMap<String, Object> data = new HashMap<>();
//        data.put("$ref", "#/definitions/" + methodName + "_Response");

    /// /        data.put("originalRef", methodName);
//
//        HashMap<String, Object> message = new HashMap<>();
//        message.put("type", "string");
//
//        properties.put("code", code);
//        properties.put("data", data);
//        properties.put("message", message);
//
//        baseResponse.put("type", "object");
//        baseResponse.put("properties", properties);
//        baseResponse.put("title", "BaseResponse<<" + methodName + ">>");
//
//        map.put("BaseResponse<<" + methodName + ">>", baseResponse);
//
//        Map<String, Object> methodObj = new HashMap<>();
//        methodObj.put("type", "object");
//        methodObj.put("properties", JSON.parseObject(requestParams, Map.class));
//        methodObj.put("title", methodName);
//
//        map.put(methodName, methodObj);
//        map.put(methodName + "_Response", responseMap);
//
//        return map;
//    }
    // end region


    private Map<String, Object> buildDoc(InterfaceInfo interfaceInfo) {
        String path = interfaceInfo.getPath();

        Map<String, Object> map = new HashMap<>();
        map.put("swagger", "2.0");
        map.put("info", Collections.singletonMap("title", "接口文档"));
        map.put("host", GATEWAY_URL);
        map.put("schemes", Collections.singletonList("http"));
        map.put("basePath", BASE_PATH);
        map.put("tags", Collections.singletonList(Collections.singletonMap("name", path)));
        map.put("paths", buildPath(interfaceInfo));
        map.put("definitions", buildDefinitions(interfaceInfo));
        map.put("x-openapi", X_OPENAPI);
        return map;
    }

    private Map<String, Object> buildPath(InterfaceInfo interfaceInfo) {
        String name = interfaceInfo.getName();
        String description = interfaceInfo.getDescription();
        String methodName = interfaceInfo.getMethodName();
        String path = interfaceInfo.getPath();
        String method = interfaceInfo.getMethod();
        String transPattern = interfaceInfo.getTransPattern();
        String requestParams = interfaceInfo.getRequestParams();

        List<Map<String, Object>> parameters = new ArrayList<>();

        if ("POST".equals(method)) {
            Map<String, Object> map = new HashMap<>();
            map.put("in", "body");
            map.put("name", methodName);
            map.put("description", description);
            map.put("required", true);
            map.put("schema", Collections.singletonMap("$ref", "#/definitions/" + methodName));
            parameters.add(map);
        } else if ("GET".equals(method)) {
            Map<String, Object> params;
            try {
                params = JSON.parseObject(requestParams, Map.class);
            } catch (Exception e) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据库requestParams格式错误！");
            }
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                Map<String, Object> map = new HashMap<>();
                map.put("in", "query");
                map.put("name", entry.getKey());
                // entry.getValue() 应该是类型字符串，如 "string", "integer"
                // 如果是 Map（比如嵌套），这里需要递归处理，但通常 GET 不支持复杂对象
                if (entry.getValue() instanceof String) {
                    map.put("type", entry.getValue());
                } else {
                    // 默认 fallback
                    map.put("type", "string");
                }
                parameters.add(map);
            }
        }

        Map<String, Object> methodMap = new HashMap<>();
        methodMap.put("tags", Collections.singletonList(name));
        methodMap.put("summary", description); // 更合理：用 description 而非 "check"
        methodMap.put("operationId", methodName);
        methodMap.put("consumes", Collections.singletonList("application/json")); // POST 用，GET 可忽略但无害
        methodMap.put("produces", Collections.singletonList("application/json"));
        methodMap.put("parameters", parameters);

        // 构建 responses
        Map<String, Object> responses = new HashMap<>();

        // 200 OK - 引用定义好的 BaseResponse_MethodName
        String baseResponseName = "BaseResponse_" + methodName;
        Map<String, Object> map200 = new HashMap<>();
        map200.put("description", "OK");
        map200.put("schema", Collections.singletonMap("$ref", "#/definitions/" + baseResponseName));

        Map<String, Object> map401 = new HashMap<>();
        map401.put("description", "Unauthorized");

        Map<String, Object> map403 = new HashMap<>();
        map403.put("description", "Forbidden");

        Map<String, Object> map404 = new HashMap<>();
        map404.put("description", "Not Found");

        responses.put("200", map200);
        responses.put("401", map401);
        responses.put("403", map403);
        responses.put("404", map404);

        methodMap.put("responses", responses);
        methodMap.put("deprecated", false);

        // 构建 path 结构
        Map<String, Object> httpMethodMap = new HashMap<>();
        HttpMethodEnum methodEnum = HttpMethodEnum.getEnumByValue(method);
        if (methodEnum == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Http请求方式不存在！");
        }

        Map<String, Object> pathMap = new HashMap<>();
        Map<String, Object> operationMap = new HashMap<>();
        operationMap.put(methodEnum.getMethod(), methodMap);
        pathMap.put(transPattern + path, operationMap);

        return pathMap;
    }

    private Map<String, Object> buildDefinitions(InterfaceInfo interfaceInfo) {
        String methodName = interfaceInfo.getMethodName();
        String requestParams = interfaceInfo.getRequestParams();
        String responseParams = interfaceInfo.getResponseParams();

        Map<String, Object> definitions = new HashMap<>();

        // 构建响应数据结构：methodName_Response
        Map<String, Object> responseProperties;
        try {
            responseProperties = JSON.parseObject(responseParams, Map.class);
        } catch (Exception e) {
            responseProperties = Collections.emptyMap();
        }

        Map<String, Object> responseObj = new HashMap<>();
        responseObj.put("type", "object");
        responseObj.put("properties", responseProperties);
        responseObj.put("title", methodName + " Response");

        String responseDefName = methodName + "_Response";
        definitions.put(responseDefName, responseObj);

        // 构建 BaseResponse<methodName>
        Map<String, Object> baseResponseProps = new HashMap<>();

        Map<String, Object> codeProp = new HashMap<>();
        codeProp.put("type", "integer");
        codeProp.put("format", "int32");

        Map<String, Object> dataProp = new HashMap<>();
        dataProp.put("$ref", "#/definitions/" + responseDefName);

        Map<String, Object> messageProp = new HashMap<>();
        messageProp.put("type", "string");

        baseResponseProps.put("code", codeProp);
        baseResponseProps.put("data", dataProp);
        baseResponseProps.put("message", messageProp);

        Map<String, Object> baseResponse = new HashMap<>();
        baseResponse.put("type", "object");
        baseResponse.put("properties", baseResponseProps);
        baseResponse.put("title", "Base Response for " + methodName);

        String baseResponseName = "BaseResponse_" + methodName;
        definitions.put(baseResponseName, baseResponse);

        // 如果是 POST，才需要请求体定义（GET 不需要，但留着也无妨）
        if ("POST".equals(interfaceInfo.getMethod())) {
            Map<String, Object> requestProperties;
            try {
                requestProperties = JSON.parseObject(requestParams, Map.class);
            } catch (Exception e) {
                requestProperties = Collections.emptyMap();
            }
            Map<String, Object> requestObj = new HashMap<>();
            requestObj.put("type", "object");
            requestObj.put("properties", requestProperties);
            requestObj.put("title", methodName + " Request");
            definitions.put(methodName, requestObj);
        }

        return definitions;
    }

}




