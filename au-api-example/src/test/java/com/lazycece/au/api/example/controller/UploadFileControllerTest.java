package com.lazycece.au.api.example.controller;

import org.junit.jupiter.api.Test;

/**
 * @author lazycece
 */
public class UploadFileControllerTest {

    private String token = "ZXlKMGVYQWlPaUpLVjFRaUxDSmhiR2NpT2lKSVV6STFOaUo5LmV5SnpkV0lpT2lKeVR6QkJRbGhPZVVGRVNtcGlNakIxWWtkR05tVlhUbXhaTWxWMVdWaFZkVmxZUW5CTWJWWTBXVmN4ZDJKSFZYVmFWelV3WVZoU05VeHRVblpqZVRWV1l6SldlVlV6Vm1saGJWWnFaRUpXTVhsTFluVlVlV0p4UVdkQlFsUkJRVWxrV0U1c1kyMDFhR0pYVmpCQlFrcE5ZVzFHTWxsVE9YTlpWelZ1VERGT01HTnRiSFZhZW5RMFkwaFJRVU5IZUdobGJteHFXbGRPYkNJc0ltbHpjeUk2SWxSUFMwVk9MVWxUVTFWRlVpSXNJbVY0Y0NJNk1UWTNPVEl3T0RNeU5pd2lhV0YwSWpveE5qYzVNakEwTnpJMmZRLl9ZT1NneTBVd0VQMV9oYTlST2FSaEFNVTNSOUNKT19CTW9SY1hkS3BFZGM";

    @Test
    public void testUpload() throws Exception {
        String filePath = ClassLoader.getSystemResource("application.yml").getFile();
        HttpHelper.getInstance().token(token).doUpload("/upload/file", filePath, "file", null, String.class);
    }
}
