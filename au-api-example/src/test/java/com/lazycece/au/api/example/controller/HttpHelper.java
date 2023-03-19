package com.lazycece.au.api.example.controller;

import com.lazycece.au.api.example.common.response.ResCode;
import com.lazycece.au.api.example.common.response.ResponseMap;
import com.lazycece.au.api.params.ParamsHolder;
import com.lazycece.au.api.params.utils.JsonUtils;
import com.lazycece.au.api.params.utils.SaltUtils;
import com.lazycece.au.api.token.TokenHolder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lazycece
 */
public class HttpHelper {

    public static final String TOKEN_HEADER = "TOKEN-HEADER";
    private static final HttpHelper HTTP_HELPER = new HttpHelper();
    private ParamsHolder paramsHolder;
    private TokenHolder tokenHolder;
    private RestTemplate restTemplate;
    private String token = "ZXlKMGVYQWlPaUpLVjFRaUxDSmhiR2NpT2lKSVV6STFOaUo5LmV5SnpkV0lpT2lKeVR6QkJRbGhPZVVGRVNtcGlNakIxWWtkR05tVlhUbXhaTWxWMVdWaFZkVmxZUW5CTWJWWTBXVmN4ZDJKSFZYVmFWelV3WVZoU05VeHRVblpqZVRWV1l6SldlVlV6Vm1saGJWWnFaRUpXTVhsTFluVlVlV0p4UVdkQlFsUkJRVWxrV0U1c1kyMDFhR0pYVmpCQlFrcE5ZVzFHTWxsVE9YTlpWelZ1VERGT01HTnRiSFZhZW5RMFkwaFJRVU5IZUdobGJteHFXbGRPYkNJc0ltbHpjeUk2SWxSUFMwVk9MVWxUVTFWRlVpSXNJbVY0Y0NJNk1UWTNPVEl3T0RNeU5pd2lhV0YwSWpveE5qYzVNakEwTnpJMmZRLl9ZT1NneTBVd0VQMV9oYTlST2FSaEFNVTNSOUNKT19CTW9SY1hkS3BFZGM";

    private HttpHelper() {
        this("http://127.0.0.1:8888/au-api");
    }

    private HttpHelper(String rootUri) {
        restTemplate = new RestTemplateBuilder()
                .rootUri(rootUri)
                .setConnectTimeout(Duration.ofSeconds(10))
                .build();
        paramsHolder = ParamsHolder.build("75HVYG0VQVDEYPLLODZUX99ZCV333EKY");
    }

    public static HttpHelper getInstance() {
        return HTTP_HELPER;
    }

    public ParamsHolder getParamsHolder() {
        return paramsHolder;
    }

    public HttpHelper token(String token) {
        this.token = token;
        return this;
    }

    public <T> T doGet(String url, Object data, Class<T> clazz) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add(TOKEN_HEADER, token);
        HttpEntity entity = new HttpEntity<>(null, headers);
        StringBuilder urlBuilder = new StringBuilder(url + "?");
        for (Map.Entry<String, String> entry : getParams(data).entrySet()) {
            urlBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                urlBuilder.toString(), HttpMethod.GET, entity, String.class);
        return parseResponse(responseEntity.getBody(), clazz);
    }

    public <T> T doPost(String url, Object data, Class<T> clazz) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add(TOKEN_HEADER, token);
        HttpEntity entity = new HttpEntity<>(getParams(data), headers);
        String response = restTemplate.postForObject(url, entity, String.class);
        return parseResponse(response, clazz);
    }

    public <T> T doPostJson(String url, Object data, Class<T> clazz) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(TOKEN_HEADER, token);
        HttpEntity entity = new HttpEntity<>(getParams(data), headers);
        String response = restTemplate.postForObject(url, entity, String.class);
        return parseResponse(response, clazz);
    }

    public ResponseEntity<ResponseMap> doPostJson(String url, Object data) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(TOKEN_HEADER, token);
        HttpEntity entity = new HttpEntity<>(getParams(data), headers);
        return restTemplate.postForEntity(url, entity, ResponseMap.class);
    }

    public <T> T doUpload(String url, String filepath, String fileField, Object data, Class<T> clazz) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.add(TOKEN_HEADER, token);
        MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<>();
        getParams(data).forEach(multiValueMap::add);
        FileSystemResource resource = new FileSystemResource(new File(filepath));
        multiValueMap.add(fileField, resource);
        HttpEntity entity = new HttpEntity<>(multiValueMap, headers);
        String response = restTemplate.postForObject(url, entity, String.class);
        return parseResponse(response, clazz);
    }

    private HashMap<String, String> getParams(Object data) throws Exception {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("time", String.valueOf(System.currentTimeMillis()));
        paramsMap.put("deviceId", "unit-test");
        paramsMap.put("version", "2.0.0");
        paramsMap.put("versionCode", "200");
        String salt = SaltUtils.generateSalt();
        paramsMap.put("salt", salt);
        if (data != null) {
            paramsMap.put("data", paramsHolder.encode(salt, JsonUtils.toJSONString(data)));
        }
        paramsMap.put("sign", paramsHolder.sign(paramsMap));
        return paramsMap;
    }

    private <T> T parseResponse(String response, Class<T> clazz) throws Exception {
        ResponseMap responseMap = JsonUtils.parseObject(response, ResponseMap.class);
        if (!ResCode.SUCCESS.equals(responseMap.get(ResponseMap.CODE_FIELD))) {
            throw new IllegalArgumentException("code: " + responseMap.get(ResponseMap.CODE_FIELD)
                    + " message: " + responseMap.get(ResponseMap.MESSAGE_FIELD));
        }
        if (null == responseMap.get(ResponseMap.DATA_FIELD)) {
            System.out.println("response data is null");
            return null;
        }
        String decodeData = paramsHolder.decode((String) responseMap.get(ResponseMap.SALT_FIELD)
                , (String) responseMap.get(ResponseMap.DATA_FIELD));
        System.out.println(decodeData);
        return JsonUtils.parseObject(decodeData, clazz);
    }
}