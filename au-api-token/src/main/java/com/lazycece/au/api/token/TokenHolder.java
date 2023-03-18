package com.lazycece.au.api.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.lazycece.au.api.token.serialize.Serializer;
import com.lazycece.au.api.token.serialize.SubjectSerializer;
import com.lazycece.au.log.AuLogger;
import com.lazycece.au.log.AuLoggerFactory;
import org.apache.commons.codec.binary.Base64;

import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * @author lazycece
 */
public class TokenHolder {

    private final AuLogger log = AuLoggerFactory.getLogger(this.getClass());
    private Serializer serializer = new SubjectSerializer();
    private String tokenHeader = "TOKEN-HEADER";
    private String issuer = "TOKEN-ISSUER";
    private long expire = 30 * 60 * 1000;
    private boolean refresh = true;
    private final String secret;

    private TokenHolder(String secret) {
        this.secret = secret;
    }

    public static TokenHolder build(String secret) {
        return new TokenHolder(secret);
    }

    public TokenHolder issuer(String issuer) {
        this.issuer = issuer;
        return this;
    }

    public TokenHolder expire(long expire) {
        this.expire = expire;
        return this;
    }

    public TokenHolder tokenHeader(String headName) {
        this.tokenHeader = headName;
        return this;
    }

    public String getTokenHeader() {
        return this.tokenHeader;
    }

    public TokenHolder refresh(boolean refresh) {
        this.refresh = refresh;
        return this;
    }

    public boolean isRefresh() {
        return refresh;
    }

    public TokenHolder serializer(Serializer serializer) {
        this.serializer = serializer;
        return this;
    }

    public String generateToken(Subject subject) throws Exception {
        if (subject == null) {
            throw new IllegalArgumentException("subject must not null");
        }
        String subjectStr = this.serializer.serialize(subject);
        String jwtToken = JWT.create()
                .withIssuer(this.issuer)
                .withSubject(subjectStr)
                .withExpiresAt(new Date(System.currentTimeMillis() + this.expire))
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .sign(Algorithm.HMAC256(this.secret));
        return base64Encode(jwtToken);
    }

    public boolean verification(String token) {
        token = base64Decode(token);
        boolean verify = false;
        try {
            DecodedJWT jwt = JWT.require(Algorithm.HMAC256(this.secret)).build().verify(token);
            verify = this.issuer.equals(jwt.getIssuer());
        } catch (Exception e) {
            log.error("verify token exception: {}", e.getMessage());
        }
        return verify;
    }

    public Subject parseToken(String token) throws Exception {
        token = base64Decode(token);
        DecodedJWT jwt = JWT.decode(token);
        return this.serializer.deserialize(jwt.getSubject());
    }

    private String base64Encode(String token) {
        byte[] base64Encode = Base64.encodeBase64(token.getBytes(StandardCharsets.UTF_8));
        return new String(base64Encode, StandardCharsets.UTF_8);
    }

    private String base64Decode(String token) {
        byte[] base64Decode = Base64.decodeBase64(token.getBytes(StandardCharsets.UTF_8));
        return new String(base64Decode, StandardCharsets.UTF_8);
    }
}
