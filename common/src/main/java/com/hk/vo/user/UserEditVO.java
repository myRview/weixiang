package com.hk.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author huangkun
 * @date 2025/6/26 16:54
 */
@Data
public class UserEditVO implements Serializable {

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "账号")
    private String account;

    @Schema(description = "用户名")
    private String userName;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "手机号")
    private String phone;
}
