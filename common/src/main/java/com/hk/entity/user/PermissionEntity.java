package com.hk.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 权限表
 * </p>
 *
 * @author hk
 * @since 2025-06-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(name = "PermissionEntity对象", description = "权限表")
@TableName("sys_permission")
public class PermissionEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "权限名称")
    private String permissionName;

    @Schema(description = "权限编码")
    private String permissionCode;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

}