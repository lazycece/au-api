package com.lazycece.au.api.params;

/**
 * @author lazycece
 * @date 2019/11/20
 */
public interface ParamsHandler {

    String validateParamsFail();

    String validateTimeFail();

    String validateSignFail();

    String getWaitEncodeData(String responseBody);

    String getResponseBody(String encodeData, String salt);
}
