package com.hk.factory.message;

import com.hk.factory.message.base.MessageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @description: 消息工厂生产者
 * @author huangkun
 * @date 2025/8/17 10:24
 */
@Component
public class MessageFactoryProducer {

    @Autowired
    private Map<String, MessageFactory> messageFactoryMap;

    public MessageFactory getFactory(String type) {
        String format = String.format("%sMessageFactory", type.toLowerCase());
        return messageFactoryMap.get(format);
    }
}
