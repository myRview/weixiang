package com.hk.storage.file.service;

import java.io.File;

/**
 * @author huangkun
 * @date 2025/7/20 19:58
 */
public interface FileStorageService {

    String upload(String filePath, File tempFile) throws Exception;

    String uploadImage(String filePath, File tempFile) throws Exception;

    void deleteFile(String filePath)throws Exception;
}
