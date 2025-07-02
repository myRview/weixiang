package com.hk.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hk.entity.user.RoleEntity;
import com.hk.vo.user.PermissionVO;
import com.hk.vo.user.RolePermissionVO;
import com.hk.vo.user.RoleVO;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author hk
 * @since 2025-06-30
 */
public interface RoleService extends IService<RoleEntity> {

    boolean saveRole(RoleVO roleVO);

    boolean deleteRole(Long roleId);

    List<PermissionVO> getRolePermission(Long roleId);

    RoleVO selectByCode(String code);

    List<RoleVO> getAll();

    boolean saveRolePermission(RolePermissionVO rolePermissionVO);

    RoleVO selectById(Long roleId);

    List<RoleEntity> selectByIds(List<Long> roleIds);
}