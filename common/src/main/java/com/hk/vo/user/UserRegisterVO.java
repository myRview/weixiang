package com.hk.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册
 * @author huangkun
 * @date 2025/6/26 10:46
 */
@Data
public class UserRegisterVO implements Serializable {

    @Schema(description = "账号")
    private String account;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "确认密码")
    private String confirmPassword;

}
