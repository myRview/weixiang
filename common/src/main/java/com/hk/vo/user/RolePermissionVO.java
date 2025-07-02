package com.hk.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author huangkun
 * @date 2025/6/30 13:50
 */
@Data
public class RolePermissionVO implements Serializable {
    @Schema(description = "角色id")
    private Long roleId;
    @Schema(description = "权限id列表")
    private List<Long> permissionIds;
}
