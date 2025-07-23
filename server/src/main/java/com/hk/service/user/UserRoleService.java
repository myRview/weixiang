package com.hk.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hk.entity.user.UserRoleEntity;

import java.util.List;

/**
* <p>
    * 用户角色表 服务类
    * </p>
*
* @author hk
* @since 2025-06-30
*/
public interface UserRoleService extends IService<UserRoleEntity> {

    UserRoleEntity selectByUserId(Long userId);

    List<UserRoleEntity> selectByUserIds(List<Long> userIds);
}