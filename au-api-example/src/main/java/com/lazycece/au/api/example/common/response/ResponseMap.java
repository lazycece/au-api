package com.lazycece.au.api.example.common.response;

import java.util.HashMap;

/**
 * @author lazycece
 */
public class ResponseMap extends HashMap<String, Object> {
    public static final String CODE_FIELD = "code";
    public static final String MESSAGE_FIELD = "message";
    public static final String DATA_FIELD = "data";
    public static final String SALT_FIELD = "salt";

    public ResponseMap() {
    }

    private ResponseMap(String code, String message) {
        this.put(CODE_FIELD, code);
        this.put(MESSAGE_FIELD, message);
    }

    /**
     * define success response map on default code and message
     *
     * @return this
     */
    public static ResponseMap success() {
        return new ResponseMap(ResCode.SUCCESS, ResMessage.SUCCESS);
    }

    /**
     * define success response map on default code and message
     *
     * @param data data
     * @return this
     */
    public static ResponseMap success(Object data) {
        ResponseMap responseMap = new ResponseMap(ResCode.SUCCESS, ResMessage.SUCCESS);
        responseMap.put(DATA_FIELD, data);
        return responseMap;
    }

    /**
     * define fail response map on default code and message
     *
     * @return this
     */
    public static ResponseMap fail() {
        return new ResponseMap(ResCode.FAIL, ResMessage.FAIL);
    }

    /**
     * define fail response map on default code
     *
     * @param message ""
     * @return this
     */
    public static ResponseMap fail(String message) {
        return new ResponseMap(ResCode.FAIL, message);
    }

    /**
     * define fail response map
     *
     * @param code    ""
     * @param message ""
     * @return this
     */
    public static ResponseMap fail(String code, String message) {
        return new ResponseMap(code, message);
    }

    /**
     * @param key   key
     * @param value value
     * @return this
     */
    public ResponseMap putting(String key, Object value) {
        this.put(key, value);
        return this;
    }
}
