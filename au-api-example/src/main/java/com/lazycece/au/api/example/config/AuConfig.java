package com.lazycece.au.api.example.config;

import com.lazycece.au.AuManager;
import com.lazycece.au.AuServletFilter;
import com.lazycece.au.api.params.ParamsHandler;
import com.lazycece.au.api.params.ParamsHolder;
import com.lazycece.au.api.params.filter.AuParamFilter;
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
        auManager.addAuFilter(auTokenFilter).includePatterns("/**").excludePatterns("/u/login");
        AuParamFilter auParamFilter = new AuParamFilter(ParamsHolder.build(SECRET), new AuParamsHandler());
        auManager.addAuFilter(auParamFilter).includePatterns("/**");
        return filterRegistrationBean;
    }


    @Bean(name = "tokenHolder")
    public TokenHolder tokenHolder() {
        return TokenHolder.build(SECRET).expire(60 * 60 * 1000);
    }

    private static class AuTokenHandler implements TokenHandler {

        @Override
        public String noToken() {
            return "token is null";
        }

        @Override
        public String invalidToken() {
            return "invalid token";
        }
    }

    private static class AuParamsHandler implements ParamsHandler {

        @Override
        public String validateParamsFail() {
            return "validate param fail";
        }

        @Override
        public String validateTimeFail() {
            return null;
        }

        @Override
        public String validateSignFail() {
            return "validate sign fail";
        }

        @Override
        public String getWaitEncodeData(String responseBody) {
            return responseBody;
        }

        @Override
        public String getResponseBody(String encodeData, String salt) {
            return null;
        }
    }
}
