package com.yy.yyapimodel.model.enums;

import org.apache.commons.lang3.ObjectUtils;

public enum HttpMethodEnum {

    GET("get", "GET"),
    POST("post", "POST"),
    PUT("put", "PUT"),
    DELETE("delete", "DELETE");

    private final String method;

    private final String value;


    HttpMethodEnum(String method, String value) {
        this.method = method;
        this.value = value;
    }

    public String getMethod() {
        return method;
    }

    public String getValue() {
        return value;
    }

    public static HttpMethodEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (HttpMethodEnum httpMethodEnum : HttpMethodEnum.values()) {
            if (httpMethodEnum.value.equals(value)) {
                return httpMethodEnum;
            }
        }
        return null;
    }
}
