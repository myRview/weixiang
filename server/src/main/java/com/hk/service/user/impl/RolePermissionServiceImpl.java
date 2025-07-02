package com.hk.service.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.entity.user.RolePermissionEntity;
import com.hk.mapper.user.RolePermissionMapper;
import com.hk.service.user.RolePermissionService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 角色权限表 服务实现类
 * </p>
 *
 * @author hk
 * @since 2025-06-30
 */
@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermissionEntity> implements RolePermissionService {
    @Override
    public boolean removeByRole(Long roleId) {
        return this.remove(new LambdaQueryWrapper<RolePermissionEntity>().eq(RolePermissionEntity::getRoleId, roleId));
    }

    @Override
    public List<RolePermissionEntity> selectByRoleId(Long roleId) {
        LambdaQueryWrapper<RolePermissionEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RolePermissionEntity::getRoleId, roleId);
        return this.list(queryWrapper);
    }

    @Override
    public List<RolePermissionEntity> selectByRoleIds(List<Long> roleIds) {
        LambdaQueryWrapper<RolePermissionEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(RolePermissionEntity::getRoleId, roleIds);
        return this.list(queryWrapper);
    }
}
