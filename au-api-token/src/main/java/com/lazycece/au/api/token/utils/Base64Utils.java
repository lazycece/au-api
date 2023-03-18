package com.lazycece.au.api.token.utils;

import org.apache.commons.codec.binary.Base64;

import java.nio.charset.StandardCharsets;

/**
 * @author lazycece
 * @date 2023/3/19
 */
public class Base64Utils {

    public static String encode(String data) {
        byte[] base64Encode = Base64.encodeBase64(data.getBytes(StandardCharsets.UTF_8));
        return new String(base64Encode, StandardCharsets.UTF_8);
    }

    public static String decode(String data) {
        byte[] base64Decode = Base64.decodeBase64(data.getBytes(StandardCharsets.UTF_8));
        return new String(base64Decode, StandardCharsets.UTF_8);
    }
}
