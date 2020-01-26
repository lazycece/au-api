package com.lazycece.au.api.params.utils;

import java.math.BigInteger;
import java.util.UUID;

/**
 * @author lazycece
 * @date 2019/11/20
 */
public class SaltUtils {

    public static String generateSalt() {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String sn = new BigInteger(uuid, 16).toString();
        return sn.substring(0, 6);
    }
}
