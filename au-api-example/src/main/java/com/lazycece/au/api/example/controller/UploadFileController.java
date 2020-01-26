package com.lazycece.au.api.example.controller;

import com.lazycece.au.api.example.common.response.ResponseMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author lazycece
 */
@RestController
@RequestMapping("/upload")
@Slf4j
public class UploadFileController {

    @PostMapping("/file")
    public Object upload(@RequestParam MultipartFile file){
        log.info("filename = {}, file-length = {}",file.getOriginalFilename(),file.getSize());
        return ResponseMap.success();
    }
}
