package com.hk.listener;

import com.hk.config.RabbitMQConfig;
import com.hk.enums.OrderStatusEnum;
import com.hk.service.order.OrderInfoService;
import com.hk.service.plan.UserPlanService;
import com.hk.mq.MessageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 消息消费者
 *
 * @author huangkun
 * @date 2025/7/15 9:56
 */
@Component
@Slf4j
public class MessageConsumer {
    @Autowired
    private OrderInfoService orderInfoService;
    @Autowired
    private UserPlanService userPlanService;

//    @RabbitListener(queues = RabbitMQConfig.DLX_QUEUE)
//    public void listenDelayMessage(MessageVO message) {
//        String receiveTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//        log.info("【消费时间: {}】消息内容: {}", receiveTime, message);
//        // 根据消息类型和内容进行处理
//        executeBusiness(message);
//
//    }


    //监听延迟队列
    @RabbitListener(queues = RabbitMQConfig.DELAYED_QUEUE)
    public void receiveDelayedMessage(MessageVO message) {
        String receiveTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        log.info("【消费时间: {}】消息内容: {}", receiveTime, message);
        executeBusiness(message);
    }

    //监听订单队列
    @RabbitListener(queues = RabbitMQConfig.ORDER_QUEUE)
    public void receiveOrderMessage(MessageVO message) {
        String receiveTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        log.info("【消费时间: {}】消息内容: {}", receiveTime, message);
        executeBusiness(message);
    }


    private void executeBusiness(MessageVO message) {
        String messageType = message.getMessageType();
        String content = message.getContent();
        Long dataId = message.getDataId();
        switch (messageType) {
            case "ORDER_CREATE":
                // 处理订单创建消息
                orderInfoService.closeOrder(dataId);
                break;
            case "ORDER_COMPLETE":
                // 处理订单完成消息
                userPlanService.saveUserPlan(dataId);
                break;
            default:
                log.error("未知消息类型: {}", messageType);
        }
    }
}
