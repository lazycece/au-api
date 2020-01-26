package com.lazycece.au.api.example.controller;

import com.lazycece.au.api.example.entity.req.LoginReq;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

/**
 * @author lazycece
 */
public class UserControllerTest {

    @Test
    public void testLogin() throws Exception {
        LoginReq req = new LoginReq();
        req.setUsername("lazycece");
        req.setPassword("000000");
        ResponseEntity responseEntity = HttpHelper.getInstance().doPostJson("/u/login", req);
        System.out.println(responseEntity.getHeaders().get(HttpHelper.TOKEN_HEADER));
    }

    @Test
    public void testInfo() throws Exception {
        HttpHelper.getInstance().doGet("/u/info",null,String.class);
    }
}
