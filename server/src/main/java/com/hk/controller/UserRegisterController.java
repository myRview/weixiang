package com.hk.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import com.hk.common.ResponseResult;
import com.hk.entity.UserEntity;
import com.hk.service.UserService;
import com.hk.utils.Md5Utils;
import com.hk.vo.user.UserAddVO;
import com.hk.vo.user.UserRegisterVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huangkun
 * @date 2025/6/26 17:10
 */
@RestController
public class UserRegisterController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseResult register(@RequestBody UserRegisterVO registerVO) {
        String account = registerVO.getAccount();
        String email = registerVO.getEmail();
        String phone = registerVO.getPhone();
        String password = registerVO.getPassword();
        String confirmPassword = registerVO.getConfirmPassword();
        UserEntity user = userService.selectOneByAccount(account);
        if (user != null) {
            return ResponseResult.fail("账号已存在");
        }
        if (!password.equals(confirmPassword)) {
            return ResponseResult.fail("两次密码不一致");
        }
        UserAddVO userAddVO = new UserAddVO();
        BeanUtil.copyProperties(registerVO, userAddVO);
        boolean save = userService.saveUser(userAddVO);
        return save ? ResponseResult.success("注册成功") : ResponseResult.fail("注册失败");

    }
}
