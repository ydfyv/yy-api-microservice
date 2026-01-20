package com.yy.yyapiinterface.api;

public interface DynamicRouteService {

    /**
     * 新增路由
     * @param id 接口方法名称
     * @param uri 服务器uri
     * @param patternPath 转发路径模式匹配
     */
    void addRoute(String id, String uri, String patternPath);

    /**
     * 删除落雨
     * @param id 接口方法名称
     */
    void deleteRoute(String id);

    /**
     * 更新路由
     * @param id 接口方法名称
     * @param uri 服务器uri
     * @param patternPath 转发路径模式匹配
     */
    void updateRoute(String id, String uri, String patternPath);
}
