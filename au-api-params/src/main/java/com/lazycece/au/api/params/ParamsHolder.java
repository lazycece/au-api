package com.lazycece.au.api.params;

import com.lazycece.au.api.params.crypt.DES3Crypto;
import com.lazycece.au.api.params.crypt.DataCrypto;
import com.lazycece.au.api.params.utils.SignatureUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author lazycece
 * @date 2019/11/20
 */
public class ParamsHolder {

    private DataCrypto dataCrypto = new DES3Crypto();
    private Class<? extends ApiParams> paramsClazz = ApiParams.class;
    /**
     * The time interval that request from client to server.
     */
    private long timeInterval = 3000;
    private final String secretKey;

    private ParamsHolder(String secretKey) {
        this.secretKey = secretKey;
    }

    public static ParamsHolder build(String secretKey) {
        return new ParamsHolder(secretKey);
    }

    public ParamsHolder dataCrypto(DataCrypto dataCrypto) {
        this.dataCrypto = dataCrypto;
        return this;
    }

    public ParamsHolder paramsClazz(Class<? extends ApiParams> clazz) {
        this.paramsClazz = clazz;
        return this;
    }

    public ParamsHolder timeInterval(long timeInterval) {
        this.timeInterval = timeInterval;
        return this;
    }

    public long getTimeInterval() {
        return this.timeInterval;
    }

    public Class<?> getParamsClazz() {
        return this.paramsClazz;
    }

    public String sign(Map<String, String> params) {
        return SignatureUtils.generate(params, this.secretKey, "sign");
    }

    public String encode(String salt, String data) throws Exception {
        String key = DigestUtils.md5Hex(salt + this.secretKey);
        byte[] bytes = this.dataCrypto.encrypt(key, data);
        byte[] base64Encode = Base64.encodeBase64(bytes);
        return new String(base64Encode, StandardCharsets.UTF_8);
    }

    public String decode(String salt, String encodeData) throws Exception {
        String key = DigestUtils.md5Hex(salt + this.secretKey);
        byte[] base64Decode = Base64.decodeBase64(encodeData.getBytes(StandardCharsets.UTF_8));
        byte[] bytes = this.dataCrypto.decrypt(key, base64Decode);
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
