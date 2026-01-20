package com.yy.yyapibackend.service.inner;

import com.yy.yyapibackend.common.ErrorCode;
import com.yy.yyapibackend.exception.BusinessException;
import com.yy.yyapibackend.service.UserService;
import com.yy.yyapiinterface.api.InnerUserService;
import com.yy.yyapimodel.model.entity.User;
import com.yy.yyapimodel.utils.SignUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @author 阿狸
 * @date 2026-01-06
 */
@DubboService
public class InnerUserServiceImpl implements InnerUserService {

    @Resource
    private UserService userService;

    @Override
    public boolean isAccessible(String accessKey, String path, String method, String signed) {

        User user = userService.lambdaQuery().eq(User::getAccessKey, accessKey).one();
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "accessKey错误");
        }
        String secretKey = user.getSecretKey();
        String sign = SignUtils.sign(secretKey, path, method);
        return sign.equals(signed);
    }

    @Override
    public String getSecretKey(String accessKey) {
        User user = userService.lambdaQuery().eq(User::getAccessKey, accessKey).one();
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "accessKey错误");
        }
        return user.getSecretKey();
    }
}
