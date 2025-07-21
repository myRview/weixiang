package com.hk.storage.file.service.impl;

import com.hk.storage.file.service.FileStorageService;
import com.hk.storage.file.manager.TencentCosManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * @author huangkun
 * @date 2025/7/21 9:22
 */
@Service("tencent")
public class TencentFileStorageImpl implements FileStorageService {

    private final TencentCosManager tencentCosManager;

    @Autowired
    public TencentFileStorageImpl(TencentCosManager tencentCosManager) {
        this.tencentCosManager = tencentCosManager;
    }

    @Override
    public String upload(String filePath, File tempFile) throws Exception {
        return tencentCosManager.putFile(filePath, tempFile);
    }

    @Override
    public String uploadImage(String filePath, File tempFile) throws Exception {
        return tencentCosManager.putImage(filePath, tempFile);
    }

    @Override
    public void deleteFile(String filePath) throws Exception {
        tencentCosManager.deleteObject(filePath);
    }
}
