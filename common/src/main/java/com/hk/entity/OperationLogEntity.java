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
 * 操作日志表
 * </p>
 *
 * @author hk
 * @since 2025-06-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(name = "OperationLogEntity对象", description = "操作日志表")
@TableName("sys_operation_log")
public class OperationLogEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

     @Schema(description = "用户id")
    private Long userId;

     @Schema(description = "用户名")
    private String username;

     @Schema(description = "操作内容")
    private String operationContent;

     @Schema(description = "操作模块")
    private String operationModule;

     @Schema(description = "IP地址")
    private String ipAddress;

     @Schema(description = "操作地址")
    private String operationAddress;

     @Schema(description = "操作时间")
    private Date operationTime;

     @Schema(description = "1-成功，0-失败")
    private Byte status;

     @Schema(description = "创建时间")
    private Date createTime;

     @Schema(description = "更新时间")
    private Date updateTime;

}