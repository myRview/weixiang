package com.hk.config;

import com.alipay.api.AlipayConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author huangkun
 * @date 2025/7/17 11:07
 */
@Configuration
@ConfigurationProperties(prefix = "alipay")
@Data
public class MyAlipayConfig {
    private String privateKey;
    private String publicKey;
    private String gatewayHost;
    private String appId;
    private String signType;
    private String notifyUrl;

    private final static String FORMAT = "json";
    private final static String CHARSET = "UTF-8";

    @Bean
    public AlipayConfig alipayConfig() {
        AlipayConfig alipayConfig = new AlipayConfig();
        alipayConfig.setServerUrl(gatewayHost);
        alipayConfig.setAppId(appId);
        alipayConfig.setPrivateKey(privateKey);
        alipayConfig.setFormat(FORMAT);
        alipayConfig.setAlipayPublicKey(publicKey);
        alipayConfig.setCharset(CHARSET);
        alipayConfig.setSignType(signType);
        return alipayConfig;
    }
}
