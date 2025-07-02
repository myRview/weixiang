package com.hk.service.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.entity.user.UserRoleEntity;
import com.hk.mapper.user.UserRoleMapper;
import com.hk.service.user.UserRoleService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户角色表 服务实现类
 * </p>
 *
 * @author hk
 * @since 2025-06-30
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRoleEntity> implements UserRoleService {
    @Override
    public UserRoleEntity selectByUserId(Long userId) {
        LambdaQueryWrapper<UserRoleEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRoleEntity::getUserId, userId);
        queryWrapper.last(" limit 1");
        return this.getOne(queryWrapper);
    }

    @Override
    public List<UserRoleEntity> selectByUserIds(List<Long> userIds) {
        LambdaQueryWrapper<UserRoleEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(UserRoleEntity::getUserId, userIds);
        queryWrapper.select(UserRoleEntity::getUserId, UserRoleEntity::getRoleId);
        return this.list(queryWrapper);
    }
}
