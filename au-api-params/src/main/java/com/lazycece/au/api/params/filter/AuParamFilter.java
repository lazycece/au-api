package com.lazycece.au.api.params.filter;

import com.lazycece.au.api.params.ApiParams;
import com.lazycece.au.api.params.ParamsHandler;
import com.lazycece.au.api.params.ParamsHolder;
import com.lazycece.au.api.params.utils.JsonUtils;
import com.lazycece.au.api.params.utils.SaltUtils;
import com.lazycece.au.context.RequestContext;
import com.lazycece.au.filter.AuFilter;
import com.lazycece.au.http.HttpServletRequestWrapper;
import com.lazycece.au.http.HttpServletResponseWrapper;
import com.lazycece.au.log.AuLogger;
import com.lazycece.au.log.AuLoggerFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lazycece
 * @date 2019/11/20
 */
@SuppressWarnings("unchecked")
public class AuParamFilter implements AuFilter {

    private static final String CONTENT_TYPE_JSON = "application/json";
    private final AuLogger log = AuLoggerFactory.getLogger(this.getClass());
    private final ParamsHandler paramsHandler;
    private final ParamsHolder paramsHolder;

    public AuParamFilter(ParamsHolder paramsHolder, ParamsHandler paramsHandler) {
        this.paramsHolder = paramsHolder;
        this.paramsHandler = paramsHandler;
    }

    @Override
    public String name() {
        return "au-param-filter";
    }

    @Override
    public boolean preHandle() throws Exception {
        log.debug("Check params ... ");
        HttpServletRequestWrapper requestWrapper = (HttpServletRequestWrapper) RequestContext.getCurrentContext().getRequest();
        // 1.parse params
        Map<String, String> paramsMap = new HashMap<>(16);
        String jsonContent;
        String contentType = requestWrapper.getContentType();
        log.debug("content-type is {}", contentType);
        if (StringUtils.isNotBlank(contentType) && contentType.contains(CONTENT_TYPE_JSON)) {
            jsonContent = new String(requestWrapper.getContent(), StandardCharsets.UTF_8);
            paramsMap.putAll(JsonUtils.parseObject(jsonContent, HashMap.class));
        } else {
            requestWrapper.getParameters().forEach((name, values) -> paramsMap.put(name, values[0]));
            jsonContent = JsonUtils.toJSONString(paramsMap);
        }
        log.debug("api params -> {}", jsonContent);
        ApiParams apiParams = (ApiParams) JsonUtils.parseObject(jsonContent, this.paramsHolder.getParamsClazz());
        // 2.check params
        if (!apiParams.validate()) {
            RequestContext.getCurrentContext().getResponse().getOutputStream().print(this.paramsHandler.validateParamsFail());
            return false;
        }
        // 3.check time
        if (System.currentTimeMillis() - apiParams.getTime() > paramsHolder.getTimeInterval()) {
            RequestContext.getCurrentContext().getResponse().getOutputStream().print(this.paramsHandler.validateTimeFail());
            return false;
        }
        // 4.check sign
        String sign = this.paramsHolder.sign(paramsMap);
        if (!sign.equals(apiParams.getSign())) {
            log.debug("sign on server is {}, and sign on client is {}", sign, apiParams.getSign());
            RequestContext.getCurrentContext().getResponse().getOutputStream().print(this.paramsHandler.validateSignFail());
            return false;
        }
        // 5.decode data
        if (StringUtils.isNotBlank(apiParams.getData())) {
            String data = this.paramsHolder.decode(apiParams.getSalt(), apiParams.getData());
            log.debug("request data -> {}", data);
            // retain basic parameters, and remove encode data.
            paramsMap.remove("data");
            if (StringUtils.isNotBlank(contentType) && contentType.contains(CONTENT_TYPE_JSON)) {
                Field contentFiled = FieldUtils.getField(HttpServletRequestWrapper.class, "content", true);
                FieldUtils.writeField(contentFiled, requestWrapper, data.getBytes(StandardCharsets.UTF_8), true);
            } else {
                paramsMap.putAll(JsonUtils.parseObject(data, HashMap.class));
            }
            Map<String, String[]> parameters = new HashMap<>();
            paramsMap.forEach((k, v) -> parameters.put(k, new String[]{v}));
            Field parametersFiled = FieldUtils.getField(HttpServletRequestWrapper.class, "parameters", true);
            FieldUtils.writeField(parametersFiled, requestWrapper, parameters, true);
        }
        log.debug("Check params success .");
        return true;
    }

    @Override
    public void postHandle() throws Exception {
        log.debug("Encode data ..");
        HttpServletResponseWrapper responseWrapper = (HttpServletResponseWrapper) RequestContext.getCurrentContext().getResponse();
        byte[] bytes = responseWrapper.getContent();
        if (bytes.length == 0) {
            log.debug("no response content.");
            return;
        }
        String content = new String(bytes, StandardCharsets.UTF_8);
        String data = this.paramsHandler.getWaitEncodeData(content);
        if (StringUtils.isBlank(data)) {
            log.debug("there is no data needs be encoded.");
            return;
        }
        String salt = SaltUtils.generateSalt();
        String encodeData = this.paramsHolder.encode(salt, data);
        String finalBody = this.paramsHandler.getResponseBody(content,encodeData, salt);
        responseWrapper.setContent(finalBody.getBytes(StandardCharsets.UTF_8));
        log.debug("Encode data success .");
    }
}
