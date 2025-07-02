package com.hk.vo.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author huangkun
 * @date 2025/6/24 18:36
 */
@Data
public class UserVO implements Serializable {

    @Schema(description = "用户Id")
    private Long id;

    @Schema(description = "账号")
    private String account;

    @Schema(description = "用户名")
    private String userName;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "0-禁用，1-启用")
    private Integer status;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "角色代码")
    private String roleCode;

    @Schema(description = "角色")
    private RoleVO roleVO;
}
