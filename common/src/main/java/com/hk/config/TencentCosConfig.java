package com.hk.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author huangkun
 * @date 2025/7/20 20:11
 */
@Data
@Configuration
@ConfigurationProperties("cos.tencent")
public class TencentCosConfig {
    /**
     * 域名
     */
    private String host;
    /**
     * secretId
     */
    private String secretId;
    /**
     * secretKey
     */

    private String secretKey;
    /**
     * 地域
     */

    private String region;
    /**
     * 桶名
     */
    private String bucket;
}
