package com.hk.scoket;

import com.alibaba.fastjson.JSONObject;
import com.hk.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author huangkun
 * @date 2025/8/12 14:47
 */
@Slf4j
@Component
public class ArticleApprovalHandler  extends TextWebSocketHandler {
    // 存储用户ID与WebSocketSession的映射
    private static final Map<String, WebSocketSession> userSessions = new ConcurrentHashMap<>();

    // 连接建立时触发
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 从会话中获取用户ID（需要根据实际认证方式获取）
        String userId = getUserIdFromSession(session);
        if (userId != null) {
            userSessions.put(userId, session);
        }
    }

    // 连接关闭时触发
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 从会话中获取用户ID（需要根据实际认证方式获取）
        String userId = getUserIdFromSession(session);
        if (userId != null) {
            userSessions.remove(userId);
        }
    }

    // 发送消息给指定用户
    public boolean sendMessageToUser(String userId, PushMessageBaseVO messageVO) {
        WebSocketSession session = userSessions.get(userId);
        if (session != null && session.isOpen()) {
            try {
                String message = JSONObject.toJSONString(messageVO);
                session.sendMessage(new TextMessage(message));
                return true;
            } catch (IOException e) {
                // 处理异常
                return false;
            }
        }
        return false;
    }

    private String getUserIdFromSession(WebSocketSession session) {
        UriComponents uriComponents = UriComponentsBuilder.fromUri(session.getUri()).build();
        String token = uriComponents.getQueryParams().getFirst("token");

        if (token == null || token.isEmpty()) {
            return null;
        }
        String userId = null;
        try {
            userId = JwtUtil.getUserId(token);
        } catch (Exception e) {
            log.error("解析token失败");
           return null;
        }
        return userId;
    }
}
