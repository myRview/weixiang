package com.hk.storage.file.manager;

import com.hk.common.ErrorCode;
import com.hk.config.TencentCosConfig;
import com.hk.exception.BusinessException;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.model.ciModel.persistence.PicOperations;
import com.qcloud.cos.region.Region;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

/**
 * @author huangkun
 * @date 2025/7/20 20:09
 */
@Component
@Slf4j
public class TencentCosManager {

    private final COSClient cosClient;

    private final TencentCosConfig tencentCosConfig;

    @Autowired
    public TencentCosManager(TencentCosConfig tencentCosConfig) {
        this.tencentCosConfig = tencentCosConfig;
        COSCredentials cred = new BasicCOSCredentials(tencentCosConfig.getSecretId(), tencentCosConfig.getSecretKey());
        ClientConfig clientConfig = new ClientConfig(new Region(tencentCosConfig.getRegion()));
        clientConfig.setHttpProtocol(HttpProtocol.https);
        this.cosClient = new COSClient(cred, clientConfig);
    }


    public PutObjectResult putObject(String filePath, File localFile) throws Exception {
        return cosClient.putObject(createPutRequest(filePath, localFile));
    }


    public String putFile(String filePath, File localFile) throws Exception {
        putObject(filePath, localFile);
        return buildFullUrl(filePath);
    }

    public PutObjectResult putBaseImage(String filePath, File localFile) throws Exception {
        PutObjectRequest request = createPutRequest(filePath, localFile);
        PicOperations picOperations = new PicOperations();
        picOperations.setIsPicInfo(1);
        request.setPicOperations(picOperations);
        try {
            return cosClient.putObject(request);
        } catch (CosClientException e) {
            e.printStackTrace();
            log.info("图片上传失败 | filePath: {}", filePath, e);
            throw new BusinessException(ErrorCode.REMOTE_SERVICE_ERROR, "图片上传失败");
        }
    }

    public String putImage(String filePath, File localFile) throws Exception {
        putBaseImage(filePath, localFile);
        return buildFullUrl(filePath);
    }

    public void deleteObject(String filePath) throws Exception {
        validateFilePath(filePath);
        cosClient.deleteObject(tencentCosConfig.getBucket(), filePath);
    }

    private void validateFilePath(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "文件路径不能为空");
        }
    }

    private PutObjectRequest createPutRequest(String filePath, File localFile) {
        validateFilePath(filePath);
        return new PutObjectRequest(tencentCosConfig.getBucket(), filePath, localFile);
    }

    private PutObjectRequest createPutRequest(String filePath, InputStream inputStream, ObjectMetadata metadata) {
        validateFilePath(filePath);
        return new PutObjectRequest(tencentCosConfig.getBucket(), filePath, inputStream, metadata);
    }

    /**
     * 构建完整url
     *
     * @param filePath
     * @return
     */
    private String buildFullUrl(String filePath) {
        return cosClient.getObjectUrl(tencentCosConfig.getBucket(), filePath).toString();
    }

}
