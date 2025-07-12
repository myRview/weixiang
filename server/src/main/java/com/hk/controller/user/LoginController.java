package com.hk.controller.user;

import com.hk.aop.log.annotation.LoginLog;
import com.hk.common.ResponseResult;
import com.hk.entity.user.UserEntity;
import com.hk.manager.TokenManager;
import com.hk.service.user.UserService;
import com.hk.utils.Md5Utils;
import com.hk.vo.user.UserCacheVo;
import com.hk.vo.user.UserLoginVO;
import com.hk.vo.user.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huangkun
 * @date 2025/6/26 16:28
 */
@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenManager tokenManager;

    @PostMapping("/login")
    @Operation(summary = "登录")
    @LoginLog(value = "login")
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
        UserVO userVO = new UserVO();
        userVO.setId(user.getId());
        userVO.setAccount(user.getAccount());
        userVO.setUserName(user.getUserName());
        String token = tokenManager.createToken(userVO);
        return ResponseResult.success(token, "登录成功");
    }

    @PostMapping("/logout")
    @Operation(summary = "退出登录")
    @LoginLog(value = "logout")
    public ResponseResult<?> logout(HttpServletRequest request) {
        UserCacheVo userCacheVo = tokenManager.getLoginUser(request);
        if (userCacheVo != null) {
            tokenManager.delLoginUser(userCacheVo.getUserId());
        }
        return ResponseResult.success("退出登录成功");
    }
}
