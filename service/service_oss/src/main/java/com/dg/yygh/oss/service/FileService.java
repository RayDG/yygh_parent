package com.dg.yygh.oss.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: DG
 * @Date: 2021/9/23 09:52
 * @Description:
 */
public interface FileService {
    // 上传文件到阿里云oss
    String upload(MultipartFile file);
}
