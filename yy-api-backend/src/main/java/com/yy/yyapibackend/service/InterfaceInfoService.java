package com.yy.yyapibackend.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yy.yyapibackend.model.dto.interfaceInfo.InterfaceInfoQueryRequest;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yy.yyapibackend.model.vo.InterfaceInfoVO;
import com.yy.yyapimodel.model.entity.InterfaceInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
* @author 阿狸
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    void validInterfaceInfo(InterfaceInfo interfaceInfo);

    InterfaceInfoVO getInterfaceInfoVO(InterfaceInfo interfaceInfo, HttpServletRequest request);

    Wrapper<InterfaceInfo> getQueryWrapper(InterfaceInfoQueryRequest interfaceInfoQueryRequest);

    Page<InterfaceInfoVO> getInterfaceInfoVOPage(Page<InterfaceInfo> interfaceInfoPage, HttpServletRequest request);

    void validateAccessible(InterfaceInfo interfaceInfo);

    Map<String, Object> getOpenApiDoc(String path);
}
