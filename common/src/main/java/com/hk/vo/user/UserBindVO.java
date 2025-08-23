package com.hk.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * @author huangkun
 * @date 2025/8/23 20:04
 */
@Data
public class UserBindVO  implements Serializable {

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "验证码")
    @NotBlank
    private String code;

    @Schema(description = "用户id")
    @NotNull
    private Long userId;
}
