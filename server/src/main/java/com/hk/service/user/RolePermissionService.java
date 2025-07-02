package com.hk.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hk.entity.user.RolePermissionEntity;

import java.util.List;

/**
* <p>
    * 角色权限表 服务类
    * </p>
*
* @author hk
* @since 2025-06-30
*/
public interface RolePermissionService extends IService<RolePermissionEntity> {

    boolean removeByRole(Long roleId);

    List<RolePermissionEntity> selectByRoleId(Long roleId);

    List<RolePermissionEntity> selectByRoleIds(List<Long> roleIds);
}