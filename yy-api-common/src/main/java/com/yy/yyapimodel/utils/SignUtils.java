package com.yy.yyapimodel.utils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

/**
 * @author 阿狸
 */
public class SignUtils {
    public static String sign(String secretKey, String path, String method) {
        String secretStr = secretKey + "." + path + "." + method;
        Digester md5 = new Digester(DigestAlgorithm.MD5);
        return md5.digestHex(secretStr);
    }
}
