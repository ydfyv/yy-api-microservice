package com.yy.yyapibackend.publish;

import com.yy.yyapibackend.event.InterfaceOnlineEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author 阿狸
 * @date 2026-01-16
 */
@Component
public class InterfaceOnlinePublisher {

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    public void publish(Long interfaceId) {
        InterfaceOnlineEvent interfaceOnlineEvent = new InterfaceOnlineEvent(this, interfaceId);
        applicationEventPublisher.publishEvent(interfaceOnlineEvent);
    }
}
