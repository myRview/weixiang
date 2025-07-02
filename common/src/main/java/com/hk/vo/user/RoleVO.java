package com.hk.vo.user;

import com.hk.entity.user.RoleEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author huangkun
 * @date 2025/6/30 11:45
 */
@Data
public class RoleVO implements Serializable {

    @Schema(description = "角色id")
    private Long id;

    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "角色编码")
    private String roleCode;

    @Schema(description = "角色权限")
    private List<PermissionVO> permissionVOList;

    public static RoleVO converter(RoleEntity roleEntity) {
        RoleVO vo = new RoleVO();
        vo.setId(roleEntity.getId());
        vo.setRoleName(roleEntity.getRoleName());
        vo.setRoleCode(roleEntity.getRoleCode());
        return vo;
    }
}
