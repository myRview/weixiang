package com.hk.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author huangkun
 * @date 2025/7/15 10:23
 */
@Configuration
public class RabbitMQConfig {

    //region 使用死信队列+TTL实现延时队列
    // 延时队列配置
    public static final String DELAY_QUEUE = "delay.queue";
    public static final String DELAY_EXCHANGE = "delay.exchange";
    public static final String DELAY_ROUTING_KEY = "delay.routingKey";
    // 死信队列配置
    public static final String DLX_QUEUE = "dlx.queue";
    public static final String DLX_EXCHANGE = "dlx.exchange";
    public static final String DLX_ROUTING_KEY = "dlx.routingKey";

    // 创建死信交换机（直连类型）
    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange(DLX_EXCHANGE);
    }

    // 创建死信队列
    @Bean
    public Queue dlxQueue() {
        return new Queue(DLX_QUEUE);
    }

    // 绑定死信队列到死信交换机
    @Bean
    public Binding dlxBinding() {
        return BindingBuilder.bind(dlxQueue()).to(dlxExchange()).with(DLX_ROUTING_KEY);
    }

    // 创建延时队列（设置TTL和死信路由）
    @Bean
    public Queue delayQueue() {
        Map<String, Object> args = new HashMap<>();
        // 设置死信交换机
        args.put("x-dead-letter-exchange", DLX_EXCHANGE);
        // 设置死信路由键
        args.put("x-dead-letter-routing-key", DLX_ROUTING_KEY);
        // 设置消息过期时间（30分钟）
        args.put("x-message-ttl", 30 * 60000);
        return new Queue(DELAY_QUEUE, true, false, false, args);
    }

    // 创建延时交换机
    @Bean
    public DirectExchange delayExchange() {
        return new DirectExchange(DELAY_EXCHANGE);
    }

    //     绑定延时队列到延时交换机
    @Bean
    public Binding delayBinding() {
        return BindingBuilder.bind(delayQueue()).to(delayExchange()).with(DELAY_ROUTING_KEY);
    }
    //endregion

    //region 使用插件实现延时队列

    // 延迟交换机名称
    public static final String DELAYED_EXCHANGE = "delayed.exchange";
    // 延迟队列名称
    public static final String DELAYED_QUEUE = "delayed.queue";
    // 路由键
    public static final String DELAYED_ROUTING_KEY = "delayed.routingKey";
    public static final String DELAY_MESSAGE = "x-delayed-message";

    // 创建延时消息交换机（延迟类型）
    @Bean
    public CustomExchange delayCustomExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(DELAYED_EXCHANGE, DELAY_MESSAGE, true, false, args);
    }

    /**
     * 创建延迟队列延迟类型）
     */
    @Bean
    public Queue delayedQueue() {
        return new Queue(DELAYED_QUEUE, true);
    }

    /**
     * 绑定队列到延迟交换机
     */
    @Bean
    public Binding bindingDelayedQueue(Queue delayedQueue, CustomExchange delayedExchange) {
        return BindingBuilder.bind(delayedQueue).to(delayedExchange).with(DELAYED_ROUTING_KEY).noargs();
    }
    //endregion

    //region 处理订单队列
    public static final String ORDER_EXCHANGE = "order.exchange";
    public static final String ORDER_QUEUE = "order.queue";
    public static final String ORDER_ROUTING_KEY = "order.routingKey";

    @Bean
    public Queue orderQueue() {
        return new Queue(ORDER_QUEUE, true, false, false);
    }

    @Bean
    public DirectExchange orderExchange() {
        return new DirectExchange(ORDER_EXCHANGE);
    }

    @Bean
    public Binding orderBinding() {
        return BindingBuilder.bind(orderQueue()).to(orderExchange()).with(ORDER_ROUTING_KEY);
    }
    //endregion

    /**
     * 消息转换器
     *
     * @return
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        return converter;
    }
}
