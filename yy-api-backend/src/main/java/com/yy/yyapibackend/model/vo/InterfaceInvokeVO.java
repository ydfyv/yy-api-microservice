package com.yy.yyapibackend.model.vo;

import lombok.Data;

/**
 * @author 阿狸
 * @date 2026-01-08
 */
@Data
public class InterfaceInvokeVO {

    /**
     * 接口名称
     */
    private String interfaceName;

    /**
     * 接口调用占比
     */
    private Double proportion;

    /**
     * 接口调用次数
     */
    private Integer invokeCount;

}
