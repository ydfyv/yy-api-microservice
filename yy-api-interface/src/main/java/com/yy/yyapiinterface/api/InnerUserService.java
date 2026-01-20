package com.yy.yyapiinterface.api;

public interface InnerUserService {

    /**
     * 判断是否可访问, 用同样的加密算法加密，比较是否和signed相同
     * @param accessKey ak
     * @param  path 请求路径
     * @param method 请求方法
     * @param signed 签名
     * @return true/false
     */
    boolean isAccessible(String accessKey, String path, String method, String signed);

    /**
     * 获取secretKey
     * @param accessKey ak
     * @return secretKey
     */
    String getSecretKey(String accessKey);
}
