package com.yy.yyapibackend.listener;

import com.yy.yyapibackend.event.InterfaceOnlineEvent;
import com.yy.yyapibackend.model.entity.InterfaceInvokeInfo;
import com.yy.yyapibackend.service.InterfaceInvokeInfoService;
import com.yy.yyapibackend.service.UserService;
import com.yy.yyapimodel.model.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 阿狸
 * @date 2026-01-16
 */
@Component
@Slf4j
public class InterfaceOnlineListener {

    @Resource
    private UserService userService;

    @Resource
    private InterfaceInvokeInfoService interfaceInvokeInfoService;

    @EventListener
    public void onApplicationEvent(InterfaceOnlineEvent event) {
        Long interfaceId = event.getInterfaceId();
        List<User> userList = userService.list();
        log.info("接口上线，开始初始化接口调用信息，接口id：{}, 每个用户提供50次调用次数", interfaceId);
        List<InterfaceInvokeInfo> interfaceInvokeInfos = new ArrayList<>();

        for (User user : userList) {
            InterfaceInvokeInfo interfaceInvokeInfo = new InterfaceInvokeInfo();
            interfaceInvokeInfo.setUserId(user.getId());
            interfaceInvokeInfo.setInterfaceInfoId(interfaceId);
            interfaceInvokeInfo.setLeftNum(50);
            interfaceInvokeInfos.add(interfaceInvokeInfo);
        }

        interfaceInvokeInfoService.saveBatch(interfaceInvokeInfos);
    }
}
