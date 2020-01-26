package com.lazycece.au.api.example.controller;

import org.junit.jupiter.api.Test;

/**
 * @author lazycece
 */
public class UploadFileControllerTest {

    private String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyTzBBQlhOeUFESmpiMjB1YkdGNmVXTmxZMlV1WVhVdVlYQnBMbVY0WVcxd2JHVXVaVzUwYVhSNUxtUnZjeTVWYzJWeVUzVmlhbVZqZEJWMXlLYnVUeWJxQWdBQlRBQUlkWE5sY201aGJXVjBBQkpNYW1GMllTOXNZVzVuTDFOMGNtbHVaenQ0Y0hRQUNHeGhlbmxqWldObCIsImlzcyI6IlRPS0VOLUlTU1VFUiIsImV4cCI6MTU4MDA1NzExOSwiaWF0IjoxNTgwMDUzNTE5fQ.jBtKG-hH_oNPXqXnwEZ_tVpViCkACm81l8INQCTdTlc";

    @Test
    public void testUpload() throws Exception {
        String filePath = ClassLoader.getSystemResource("application.yml").getFile();
        HttpHelper.getInstance().token(token).doUpload("/upload/file", filePath, "file", null, String.class);
    }
}
