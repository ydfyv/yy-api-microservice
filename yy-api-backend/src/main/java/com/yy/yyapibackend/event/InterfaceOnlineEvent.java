package com.yy.yyapibackend.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author 阿狸
 * @date 2026-01-16
 */
public class InterfaceOnlineEvent extends ApplicationEvent {

    private Long interfaceId;


    public InterfaceOnlineEvent(Object source, Long interfaceId) {
        super(source);
        this.interfaceId = interfaceId;
    }

    public Long getInterfaceId() {
        return interfaceId;
    }

}
