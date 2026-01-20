package com.yy.yyapiinterface.api;

public interface InnerInterfaceInfoService {
    /**
     * 验证接口是否可访问
     * @param path 路径
     * @param method 请求方式
     * @return 是否可访问
     */
    boolean validateInterfaceAccess(String path, String method);

    /**
     * 调用成功，接口调用次数 + 1
     * @param accessKey ak
     * @param path 路径
     * @return 是否成功
     */
    boolean increaseInvokeCount(String accessKey, String path);
}
