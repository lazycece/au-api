package com.lazycece.au.api.params.crypt;

/**
 * Symmetric encryption algorithm for transferring data
 *
 * @author lazycece
 * @date 2019/11/20
 */
public interface DataCrypto {

    byte[] encrypt(String key, String data) throws Exception;

    byte[] decrypt(String key, byte[] data) throws Exception;
}
