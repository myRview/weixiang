package com.hk.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 登录日志表
 * </p>
 *
 * @author hk
 * @since 2025-06-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(name = "LoginLogEntity对象", description = "登录日志表")
@TableName("sys_login_log")
public class LoginLogEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "用户名")
    private String username;

     @Schema(description = "用户id")
    private Long userId;

     @Schema(description = "IP地址")
    private String ipAddress;

     @Schema(description = "登录地点")
    private String location;

     @Schema(description = "登录设备")
    private String device;

     @Schema(description = "登录时间")
    private Date loginTime;

     @Schema(description = "1-登录成功，0-登录失败")
    private Integer status;

     @Schema(description = "创建时间")
    private Date createTime;

     @Schema(description = "更新时间")
    private Date updateTime;

}