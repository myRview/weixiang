package com.hk.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * @author huangkun
 * @date 2025/8/5 22:24
 */
@Configuration
@EnableWebSocketMessageBroker // 启用WebSocket消息代理
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 配置消息代理，客户端订阅的前缀
        config.enableSimpleBroker("/topic", "/user");
        // 配置客户端发送消息的前缀
        config.setApplicationDestinationPrefixes("/app");
        // 配置用户目的地前缀，用于给指定用户发送消息
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 注册WebSocket端点，客户端通过该端点连接
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*") // 允许跨域
                .withSockJS(); // 启用SockJS，用于不支持WebSocket的浏览器
    }
}
