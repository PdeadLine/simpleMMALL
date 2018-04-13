package com.tmall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * create by sintai
 */

public interface IFileService {
    public String upload(MultipartFile file, String path);

}
