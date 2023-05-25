package com.lazycece.au.api.example.entity.req;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;

/**
 * @author lazycece
 */
@Getter
@Setter
public class LoginReq {
    @NotBlank(message = "參數username不能为空")
    private String username;
    @NotBlank(message = "參數password不能为空")
    private String password;
}
