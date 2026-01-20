package com.yy.yyapibackend.listener;

import com.yy.yyapibackend.service.InterfaceInfoService;
import com.yy.yyapibackend.utils.GatewayRouteUtils;
import com.yy.yyapimodel.model.entity.InterfaceInfo;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 阿狸
 * @date 2026-01-13
 */
@Component
public class GatewayRefreshRoutesListener {

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    private GatewayRouteUtils gatewayRouteUtils;

    @EventListener
    public void handleContextRefresh(ContextRefreshedEvent event) {
        // 动态创建网关路由
        List<InterfaceInfo> interfaceInfoList = interfaceInfoService.lambdaQuery().eq(InterfaceInfo::getStatus, 1).list();

        for (InterfaceInfo interfaceInfo : interfaceInfoList) {
            String methodName = interfaceInfo.getMethodName();
            String serverUri = interfaceInfo.getServerUri();
            String transPattern = interfaceInfo.getTransPattern();
            gatewayRouteUtils.addRoute(methodName, serverUri, transPattern);
        }
    }
}
