package com.hk.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author huangkun
 * @date 2025/7/21 14:32
 */
@Data
@Configuration
@ConfigurationProperties(prefix="cos.aliyun")
public class AliYunOssConfig {

    private String accessKeyId;
    private String accessKeySecret;
    private String endpoint;
    private String bucket;
    private String region;
}
