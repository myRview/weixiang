package com.hk.factory.message.base;

import com.hk.utils.CheckUtil;
import org.springframework.stereotype.Component;

/**
 * @description: 邮件消息工厂
 * @author huangkun
 * @date 2025/8/17 10:26
 */
@Component("emailMessageFactory")
public class EmailMessageFactory implements MessageFactory {
    @Override
    public String sendMessage(String target) {
        if (!CheckUtil.checkEmail(target)) {
            return null;
        }
        //TODO: 调用邮件服务
        return null;

    }
}
