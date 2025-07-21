package com.hk.storage.file.manager;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.comm.Protocol;
import com.aliyun.oss.common.comm.SignVersion;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.aliyun.oss.model.VoidResult;
import com.hk.config.AliYunOssConfig;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;

/**
 * @author huangkun
 * @date 2025/7/21 14:43
 */
@Component
public class AliYunOssManager {

    private final AliYunOssConfig aliYunOssConfig;
    private final OSS ossClient;

    public AliYunOssManager(AliYunOssConfig aliYunOssConfig) {
        this.aliYunOssConfig = aliYunOssConfig;
        // 创建 ClientBuilderConfiguration 实例，用于配置 OSS 客户端参数
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        // 设置签名算法版本为 V4
        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);
        // 设置使用 HTTPS 协议访问 OSS，保证传输安全性
        clientBuilderConfiguration.setProtocol(Protocol.HTTPS);
//        使用DefaultCredentialProvider方法直接设置AK和SK
        CredentialsProvider credentialsProvider = new DefaultCredentialProvider(aliYunOssConfig.getAccessKeyId(), aliYunOssConfig.getAccessKeySecret());
        this.ossClient = OSSClientBuilder.create().endpoint(aliYunOssConfig.getEndpoint()).credentialsProvider(credentialsProvider).clientConfiguration(clientBuilderConfiguration).region(aliYunOssConfig.getRegion()).build();
    }

    public PutObjectResult putObject(String filePath, File localFile) throws Exception {
        PutObjectResult result = ossClient.putObject(aliYunOssConfig.getBucket(), filePath, localFile);
        return result;
    }

    public PutObjectResult putObject(String filePath, File localFile, ObjectMetadata metadata) throws Exception {
        PutObjectResult result = ossClient.putObject(aliYunOssConfig.getBucket(), filePath, localFile, metadata);
        return result;
    }

    public PutObjectResult putObject(String filePath, InputStream inputStream, ObjectMetadata metadata) throws Exception {
        PutObjectResult result = ossClient.putObject(aliYunOssConfig.getBucket(), filePath, inputStream, metadata);
        return result;
    }

    public PutObjectResult putObject(String filePath, InputStream inputStream) throws Exception {
        PutObjectResult result = ossClient.putObject(aliYunOssConfig.getBucket(), filePath, inputStream);
        return result;
    }

    public String getObjectUrl(String filePath) throws Exception {
        OSSObject ossObject = ossClient.getObject(aliYunOssConfig.getBucket(), filePath);
        return ossObject.getResponse().getUri();
    }

    public void deleteObject(String filePath) throws Exception {
        VoidResult result = ossClient.deleteObject(aliYunOssConfig.getBucket(), filePath);
    }

}
