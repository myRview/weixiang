package com.hk.utils;

import com.hk.scoket.BaseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @author huangkun
 * @date 2025/8/5 22:33
 */
@Component
public class WebSocketMessageUtil {
    private final SimpMessagingTemplate messagingTemplate;
    // 消息推送的目的地后缀
    private static final String DESTINATION = "/notification";

    @Autowired
    public WebSocketMessageUtil(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }


    /**
     * 向指定用户发送消息
     *
     * @param userId  接收消息的用户ID
     * @param message 消息对象
     */
    public void sendMessageToUser(String userId, BaseMessage message) {
        if (userId == null || message == null) {
            throw new IllegalArgumentException("用户ID和消息对象不能为空");
        }
        System.out.println("userId="+userId);
        System.out.println("message="+message);
        // 发送消息到指定用户的目的地
        messagingTemplate.convertAndSendToUser(userId, DESTINATION, message);
    }

    /**
     * 向多个用户发送消息
     *
     * @param userIds 接收消息的用户ID列表
     * @param message 消息对象
     */
    public void sendMessageToUsers(Collection<String> userIds, BaseMessage message) {
        if (userIds == null || message == null) {
            throw new IllegalArgumentException("用户ID列表和消息对象不能为空");
        }
        userIds.forEach(userId -> sendMessageToUser(userId, message));
    }
}
