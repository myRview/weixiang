package com.hk.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户登录对象
 *
 * @author huangkun
 * @date 2025/6/26 9:58
 */
@Data
public class UserLoginVO {

    @Schema(description = "账号")
    private String account;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "验证码")
    private String code;


}
