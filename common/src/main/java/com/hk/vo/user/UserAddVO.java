package com.hk.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author huangkun
 * @date 2025/6/27 11:11
 */
@Data
public class UserAddVO {

    @Schema(description = "账号")
    private String account;

    @Schema(description = "用户名")
    private String userName;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "确认密码")
    private String confirmPassword;

    @Schema(description = "角色id")
    private Long roleId;


}
