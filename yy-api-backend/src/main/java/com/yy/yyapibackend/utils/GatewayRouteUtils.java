package com.yy.yyapibackend.utils;

import com.yy.yyapiinterface.api.DynamicRouteService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

/**
 * @author 阿狸
 * @date 2026-01-13
 */
@Component
public class GatewayRouteUtils {

    @DubboReference
    private DynamicRouteService dynamicRouteService;

    public void addRoute(String methodName, String uri, String pattern) {
        dynamicRouteService.addRoute(methodName, uri, pattern);
    }

    public void deleteRoute(String methodName) {
        dynamicRouteService.deleteRoute(methodName);
    }

    public void updateRoute(String methodName, String uri, String pattern) {
        dynamicRouteService.updateRoute(methodName, uri, pattern);
    }

}
