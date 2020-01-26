package com.lazycece.au.api.token.filter;

import com.lazycece.au.api.token.Subject;
import com.lazycece.au.api.token.SubjectContext;
import com.lazycece.au.api.token.TokenHandler;
import com.lazycece.au.api.token.TokenHolder;
import com.lazycece.au.context.RequestContext;
import com.lazycece.au.filter.AuFilter;
import com.lazycece.au.log.AuLogger;
import com.lazycece.au.log.AuLoggerFactory;
import com.lazycece.au.utils.RequestUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author lazycece
 */
public class AuTokenFilter implements AuFilter {

    private final AuLogger log = AuLoggerFactory.getLogger(this.getClass());
    private final TokenHolder tokenHolder;
    private final TokenHandler tokenHandler;

    public AuTokenFilter(TokenHolder tokenHolder, TokenHandler tokenHandler) {
        this.tokenHolder = tokenHolder;
        this.tokenHandler = tokenHandler;
    }

    @Override
    public String name() {
        return "au-token-filter";
    }

    @Override
    public boolean preHandle() throws Exception {
        log.debug("To auth token ... ");
        String token = RequestUtils.getInstance().getHeader(this.tokenHolder.getTokenHeader());
        if (StringUtils.isBlank(token)) {
            log.warn("Auth token fail, token is null");
            RequestContext.getCurrentContext().getResponse().getOutputStream().print(this.tokenHandler.noToken());
            return false;
        }
        if (!this.tokenHolder.verification(token)) {
            log.warn("Auth token fail, invalid token");
            RequestContext.getCurrentContext().getResponse().getOutputStream().print(this.tokenHandler.invalidToken());
            return false;
        }
        Subject subject = this.tokenHolder.parseToken(token);
        SubjectContext.setContext(subject);
        log.debug("Auth token success .");
        return true;
    }

    @Override
    public void postHandle() throws Exception {
        Subject subject = SubjectContext.getContext();
        if(subject!=null){
            SubjectContext.remove();
            if(this.tokenHolder.isRefresh()){
                String token = this.tokenHolder.generateToken(subject);
                RequestContext.getCurrentContext().getResponse().setHeader(this.tokenHolder.getTokenHeader(),token);
            }
        }
    }
}
