package com.hk.factory.message.base;

import com.hk.utils.CheckUtil;
import org.springframework.stereotype.Component;

/**
 * @description: 短信消息工厂
 * @author huangkun
 * @date 2025/8/17 10:26
 */
@Component("smsMessageFactory")
public class SMSMessageFactory implements MessageFactory {
    @Override
    public String sendMessage(String target) {
        if (!CheckUtil.checkPhone(target)) {
            return null;
        }
        //TODO:调用短信服务
        //从0-9，a-Z的范围内随机生成4位验证码
        String code = String.valueOf((int) ((Math.random() * 9 + 1) * 1000));
        return code;
    }
}
