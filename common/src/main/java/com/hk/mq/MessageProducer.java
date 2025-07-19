package com.hk.mq;

import com.hk.config.RabbitMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 消息生产者
 *
 * @author huangkun
 * @date 2025/7/14 18:08
 */
@Component
@Slf4j
public class MessageProducer {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public MessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    /**
     * 发送消息（支持泛型）
     *
     * @param routingKey 路由键
     * @param message    消息对象
     * @param <T>        消息类型
     */
    public <T> void send(String routingKey, T message) {
        rabbitTemplate.convertAndSend(routingKey, message);
    }


    /**
     * 发送消息（支持泛型）
     *
     * @param exchange   交换机
     * @param routingKey 路由键
     * @param message    消息对象
     * @param <T>        消息类型
     */
    public <T> void send(String exchange, String routingKey, T message) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }

    /**
     * 发送延时消息
     *
     * @param exchange   交换机
     * @param routingKey 路由键
     * @param message    消息内容
     * @param message    消息内容
     * @param delayMs    延时时间（毫秒）
     */
    public <T> void sendDelayMessage(String exchange, String routingKey, T message, int delayMs) {
        // 记录发送时间
        String sendTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        rabbitTemplate.convertAndSend(
                exchange, routingKey,
                message,
                msg -> {
                    // 设置消息过期时间（毫秒）
                    msg.getMessageProperties().setExpiration(String.valueOf(delayMs));
                    return msg;
                }
        );
        log.info("【发送时间: {}】发送延时消息: {}，延时时间: {}ms", sendTime, message, delayMs);
    }

    /**
     * 发送延迟消息(插架)
     *
     * @param message 消息内容
     * @param delayMs 延迟时间（毫秒）
     */
    public <T> void sendDelayedMessage(T message, int delayMs) {
        // 记录发送时间
        String sendTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.DELAYED_EXCHANGE,
                RabbitMQConfig.DELAYED_ROUTING_KEY,
                message,
                msg -> {
                    // 设置延迟时间（毫秒）
                    msg.getMessageProperties().setHeader("x-delay", delayMs);
                    return msg;
                }
        );
        log.info("【发送时间: {}】发送延时消息: {}，延时时间: {}ms", sendTime, message, delayMs);
    }

    /**
     * 接收消息（支持泛型）
     *
     * @param queueName 队列名称
     * @param clazz     目标类型
     * @param <T>       返回类型
     * @return 消息对象
     */
    public <T> T receive(String queueName, Class<T> clazz) {
        Object result = rabbitTemplate.receiveAndConvert(queueName);
        return clazz.isInstance(result) ? clazz.cast(result) : null;
    }
}
