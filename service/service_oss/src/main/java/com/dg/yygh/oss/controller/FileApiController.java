package com.dg.yygh.oss.controller;

import com.dg.yygh.common.result.Result;
import com.dg.yygh.oss.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.sql.ResultSet;

/**
 * @Author: DG
 * @Date: 2021/9/23 09:48
 * @Description:
 */
@RestController
@RequestMapping("/api/oss/file")
public class FileApiController {

    @Autowired
    FileService fileService;

    // 上传文件到阿里云oss
    @PostMapping("fileUpload")
    public Result fileUpload(MultipartFile file) {
        // 获取上传文件
        String url = fileService.upload(file);
        return Result.ok(url);
    }

}
