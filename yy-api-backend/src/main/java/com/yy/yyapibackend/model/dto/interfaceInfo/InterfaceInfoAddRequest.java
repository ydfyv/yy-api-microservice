package com.yy.yyapibackend.model.dto.interfaceInfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求
 *
 * @author 阿狸
 */
@Data
public class InterfaceInfoAddRequest implements Serializable {

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 接口地址
     */
    private String path;

    /**
     * 服务地址
     */
    private String serverUri;

    /**
     * 转发匹配路径
     */
    private String transPattern;

    /**
     * 请求参数
     */
    private String requestParams;

    /**
     * 响应参数
     */
    private String responseParams;

    /**
     * 状态（0-关闭，1-开启）
     */
    private String status;

    /**
     * 请求方法
     */
    private String method;

    private static final long serialVersionUID = 1L;
}