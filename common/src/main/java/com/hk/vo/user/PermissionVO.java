package com.hk.vo.user;

import com.hk.entity.user.PermissionEntity;
import com.hk.entity.user.RolePermissionEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author hk
 * @since 2025-06-30
 */
@Data
public class PermissionVO implements Serializable {


    @Schema(description = "权限id")
    private Long id;

    @Schema(description = "权限名称")
    private String permissionName;

    @Schema(description = "权限编码")
    private String permissionCode;

    public static PermissionVO converter(PermissionEntity permission) {
        PermissionVO vo = new PermissionVO();
        vo.setId(permission.getId());
        vo.setPermissionName(permission.getPermissionName());
        vo.setPermissionCode(permission.getPermissionCode());
        return vo;
    }
}