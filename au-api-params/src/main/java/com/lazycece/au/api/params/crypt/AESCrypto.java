package com.lazycece.au.api.params.crypt;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

/**
 * AES ECB encrypt and decrypt
 *
 * @author lazycece
 * @date 2019/11/20
 */
public class AESCrypto implements DataCrypto {

    @Override
    public byte[] encrypt(final String secret, String data) throws Exception {
        return crypto(secret, Cipher.ENCRYPT_MODE, data.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public byte[] decrypt(final String secret, byte[] data) throws Exception {
        return crypto(secret, Cipher.DECRYPT_MODE, data);
    }

    private byte[] crypto(final String secret, int mode, byte[] data) throws Exception {
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(secret.getBytes(StandardCharsets.UTF_8));
        generator.init(128, random);
        SecretKey aesKey = new SecretKeySpec(generator.generateKey().getEncoded(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(mode, aesKey);
        return cipher.doFinal(data);
    }
}