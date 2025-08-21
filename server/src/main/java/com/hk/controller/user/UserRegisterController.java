package com.hk.controller.user;

import com.hk.cache.RedisService;
import com.hk.common.ResponseResult;
import com.hk.factory.message.base.MessageFactory;
import com.hk.factory.message.MessageFactoryProducer;
import com.hk.factory.message.enums.MessageFactoryType;
import com.hk.service.user.UserService;
import com.hk.vo.user.UserRegisterVO;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author huangkun
 * @date 2025/6/26 17:10
 */
@RestController
public class UserRegisterController {

    @Autowired
    private UserService userService;
    @Autowired
    private MessageFactoryProducer messageFactoryProducer;
    @Autowired
    private RedisService<String> redisService;

    @PostMapping("/register")
    @Operation(summary = "注册")
    public ResponseResult register(@RequestBody UserRegisterVO registerVO) {
        boolean save = userService.register(registerVO);
        return save ? ResponseResult.success("注册成功") : ResponseResult.fail("注册失败");

    }


    @PostMapping("/send/code")
    @Operation(summary = "获取验证码")
    public ResponseResult<?> sendCode(@RequestBody UserRegisterVO registerVO) {

        String email = registerVO.getEmail();
        String phone = registerVO.getPhone();
        MessageFactory factory = null;
        String target = null;
        if (StringUtils.isNotBlank(email)) {
            factory = messageFactoryProducer.getFactory(MessageFactoryType.EMAIL.name());
            target = email;
        }
        if (StringUtils.isNotBlank(phone)) {
            factory = messageFactoryProducer.getFactory(MessageFactoryType.SMS.name());
            target = phone;
        }
        if (factory == null) {
            return ResponseResult.fail("邮箱或手机号不能为空");
        }
        String code = factory.sendMessage(target);
        System.out.println("验证码：" + code);
        redisService.setWithExpire(target, code, 60, TimeUnit.SECONDS);
        return ResponseResult.success(code);
    }
}
