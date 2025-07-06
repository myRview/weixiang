package com.hk.controller.user;

import cn.hutool.core.bean.BeanUtil;
import com.hk.common.ResponseResult;
import com.hk.entity.user.UserEntity;
import com.hk.enums.UserRoleEnum;
import com.hk.service.user.RoleService;
import com.hk.service.user.UserService;
import com.hk.vo.user.RoleVO;
import com.hk.vo.user.UserAddVO;
import com.hk.vo.user.UserRegisterVO;
import org.apache.commons.lang3.StringUtils;
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
    @Autowired
    private RoleService roleService;

    @PostMapping("/register")
    public ResponseResult register(@RequestBody UserRegisterVO registerVO) {
        String account = registerVO.getAccount();
        String email = registerVO.getEmail();
        String phone = registerVO.getPhone();
        String password = registerVO.getPassword();
        String confirmPassword = registerVO.getConfirmPassword();
        if(StringUtils.isBlank(password)|| StringUtils.isBlank(confirmPassword)){
            return ResponseResult.fail("密码不能为空");
        }
        UserEntity user = userService.selectOneByAccount(account);
        if (user != null) {
            return ResponseResult.fail("账号已存在");
        }
        if (!password.equals(confirmPassword)) {
            return ResponseResult.fail("两次密码不一致");
        }
        RoleVO roleVO = roleService.selectByCode(UserRoleEnum.USER.getValue());
        UserAddVO userAddVO = new UserAddVO();
        BeanUtil.copyProperties(registerVO, userAddVO);
        userAddVO.setRoleId(roleVO.getId());
        boolean save = userService.saveUser(userAddVO);
        return save ? ResponseResult.success("注册成功") : ResponseResult.fail("注册失败");

    }
}
