package com.lazycece.au.api.example.config;

import com.lazycece.au.AuManager;
import com.lazycece.au.AuServletFilter;
import com.lazycece.au.api.example.common.response.ResCode;
import com.lazycece.au.api.example.common.response.ResponseMap;
import com.lazycece.au.api.example.entity.dos.ApiParams;
import com.lazycece.au.api.params.ParamsHandler;
import com.lazycece.au.api.params.ParamsHolder;
import com.lazycece.au.api.params.filter.AuParamFilter;
import com.lazycece.au.api.params.filter.MultiPartRequestFilter;
import com.lazycece.au.api.params.utils.JsonUtils;
import com.lazycece.au.api.token.TokenHandler;
import com.lazycece.au.api.token.TokenHolder;
import com.lazycece.au.api.token.filter.AuTokenFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lazycece
 */
@Configuration
public class AuConfig {

    private static final String SECRET = "75HVYG0VQVDEYPLLODZUX99ZCV333EKY";

    @Bean
    public FilterRegistrationBean<AuServletFilter> filterRegistrationBean(TokenHolder tokenHolder) {
        FilterRegistrationBean<AuServletFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new AuServletFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setOrder(1);

        AuManager auManager = AuManager.getInstance();
        AuTokenFilter auTokenFilter = new AuTokenFilter(tokenHolder, new AuTokenHandler());
        auManager.addAuFilter(auTokenFilter).includePatterns("/**").excludePatterns("/u/login").order(1);
        auManager.addAuFilter(MultiPartRequestFilter.class).includePatterns("/**").order(2);
        AuParamFilter auParamFilter = new AuParamFilter(ParamsHolder.build(SECRET).paramsClazz(ApiParams.class), new AuParamsHandler());
        auManager.addAuFilter(auParamFilter).includePatterns("/**").order(3);
        return filterRegistrationBean;
    }


    @Bean(name = "tokenHolder")
    public TokenHolder tokenHolder() {
        return TokenHolder.build(SECRET).expire(60 * 60 * 1000);
    }

    private static class AuTokenHandler implements TokenHandler {

        @Override
        public String noToken() {
            return JsonUtils.toJSONString(ResponseMap.fail(ResCode.INVALID_TOKEN,"token is null"));
        }

        @Override
        public String invalidToken() {
            return JsonUtils.toJSONString(ResponseMap.fail(ResCode.INVALID_TOKEN,"invalid token"));
        }
    }

    private static class AuParamsHandler implements ParamsHandler {

        @Override
        public String validateParamsFail() {
            return JsonUtils.toJSONString(ResponseMap.fail("validate param fail"));
        }

        @Override
        public String validateTimeFail() {
            return JsonUtils.toJSONString(ResponseMap.fail("invalid request"));
        }

        @Override
        public String validateSignFail() {
            return JsonUtils.toJSONString(ResponseMap.fail("validate sign fail"));
        }

        @Override
        public String getWaitEncodeData(String responseBody) {
            ResponseMap responseMap = JsonUtils.parseObject(responseBody,ResponseMap.class);
            return JsonUtils.toJSONString(responseMap.get(ResponseMap.DATA_FIELD));
        }

        @Override
        public String getResponseBody(String responseBody, String encodeData, String salt) {
            ResponseMap responseMap = JsonUtils.parseObject(responseBody,ResponseMap.class);
            responseMap.putting(ResponseMap.DATA_FIELD,encodeData)
                    .putting(ResponseMap.SALT_FIELD,salt);
            return JsonUtils.toJSONString(responseMap);
        }
    }
}
