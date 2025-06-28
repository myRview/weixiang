package com.hk.param;

import com.hk.common.PageBaseVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author huangkun
 * @date 2025/6/24 18:42
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserSearchParam extends PageBaseVO {
    @Schema(description = "账号")
    private String account;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "0-禁用，1-启用")
    private Integer status;


}
