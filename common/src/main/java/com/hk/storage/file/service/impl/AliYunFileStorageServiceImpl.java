package com.hk.storage.file.service.impl;

import com.aliyun.oss.model.PutObjectResult;
import com.hk.storage.file.manager.AliYunOssManager;
import com.hk.storage.file.service.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * @author huangkun
 * @date 2025/7/21 9:27
 */
@Slf4j
@Service("aliyun")
public class AliYunFileStorageServiceImpl implements FileStorageService {
    @Autowired
    private AliYunOssManager aliYunOssManager;

    @Override
    public String upload(String filePath, File tempFile) throws Exception {
        PutObjectResult result = aliYunOssManager.putObject(filePath, tempFile);
        return aliYunOssManager.getObjectUrl(filePath);
    }

    @Override
    public String uploadImage(String filePath, File tempFile) throws Exception {
        PutObjectResult result = aliYunOssManager.putObject(filePath, tempFile);
        return aliYunOssManager.getObjectUrl(filePath);
    }

    @Override
    public void deleteFile(String filePath) throws Exception {
        aliYunOssManager.deleteObject(filePath);
    }
}
