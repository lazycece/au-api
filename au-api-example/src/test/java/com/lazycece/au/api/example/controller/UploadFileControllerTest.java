package com.lazycece.au.api.example.controller;

import org.junit.jupiter.api.Test;

/**
 * @author lazycece
 */
public class UploadFileControllerTest {

    private String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ7XCJ1c2VySWRcIjoxNyxcInRlbGVwaG9uZVwiOlwiMTUxODIzMDk4MDZcIixcImNoYW5uZWxcIjpcImRlZmF1bHRcIixcInVzZXJOdW1iZXJcIjpcIjEwMDAwMlwifSIsImlzcyI6IlNBUy1RSEQiLCJleHAiOjE1Nzg5OTUwODksImlhdCI6MTU3ODkwODY4OX0.VJ_X71wnly4-4L74zjCwdFIZ1Sci9aBUSIoDn6aG9RY";

    @Test
    public void testUpload() throws Exception {
        String filePath = ClassLoader.getSystemResource("test-upload.wav").getFile();
        HttpHelper.getInstance().token(token).doUpload("/upload/file", filePath, "file", null, String.class);
    }
}
