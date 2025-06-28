package com.hk.controller;

import com.hk.common.ResponseResult;
import com.hk.entity.UserEntity;
import com.hk.service.UserService;
import com.hk.utils.Md5Utils;
import com.hk.vo.user.UserLoginVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huangkun
 * @date 2025/6/26 16:28
 */
@Tag(name = "登录", description = "登录接口")
@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseResult<?> login(@RequestBody UserLoginVO loginVO) {
        String account = loginVO.getAccount();
        String email = loginVO.getEmail();
        String phone = loginVO.getPhone();
        String password = loginVO.getPassword();
        String code = loginVO.getCode();
        UserEntity user = userService.selectOneByAccount(account);
        if (user == null) {
            return ResponseResult.fail("用户不存在");
        }
        String salt = user.getSalt();
        String md5 = Md5Utils.md5(salt, password);
        if (!md5.equals(user.getPassword())){
            return ResponseResult.fail("密码错误");
        }
        return ResponseResult.success();
    }
}
