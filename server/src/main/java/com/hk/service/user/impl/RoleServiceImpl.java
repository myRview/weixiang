package com.hk.service.user.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.common.ErrorCode;
import com.hk.entity.user.RoleEntity;
import com.hk.entity.user.RolePermissionEntity;
import com.hk.exception.BusinessException;
import com.hk.mapper.user.RoleMapper;
import com.hk.service.user.PermissionService;
import com.hk.service.user.RolePermissionService;
import com.hk.service.user.RoleService;
import com.hk.vo.user.PermissionVO;
import com.hk.vo.user.RolePermissionVO;
import com.hk.vo.user.RoleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author hk
 * @since 2025-06-30
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, RoleEntity> implements RoleService {

    @Autowired
    private RolePermissionService rolePermissionService;

    @Autowired
    private PermissionService permissionService;


    @Override
    public boolean saveRole(RoleVO roleVO) {
        LambdaQueryWrapper<RoleEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleEntity::getRoleCode, roleVO.getRoleCode());
        if (this.count(queryWrapper) > 0) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "角色已存在");
        }
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setRoleCode(roleVO.getRoleCode());
        roleEntity.setRoleName(roleVO.getRoleName());
        return this.save(roleEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRole(Long roleId) {
        RoleEntity role = this.getById(roleId);
        if (role == null) {
            return false;
        }
        rolePermissionService.removeByRole(roleId);
        return this.removeById(roleId);
    }

    @Override
    public List<PermissionVO> getRolePermission(Long roleId) {
        RoleEntity role = this.getById(roleId);
        if (role == null) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "角色不存在");
        }
        List<RolePermissionEntity> permissionEntityList = rolePermissionService.selectByRoleId(roleId);
        if (CollectionUtil.isNotEmpty(permissionEntityList)) {
            List<Long> permissionIds = permissionEntityList.stream().map(RolePermissionEntity::getPermissionId).collect(Collectors.toList());
            return permissionService.selectByIds(permissionIds);
        }
        return new ArrayList<>();
    }

    @Override
    public RoleVO selectByCode(String code) {
        LambdaQueryWrapper<RoleEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleEntity::getRoleCode, code);
        queryWrapper.last(" limit 1");
        RoleEntity roleEntity = this.getOne(queryWrapper);
        if (roleEntity == null) return null;
        RoleVO roleVo = RoleVO.converter(roleEntity);
        List<RolePermissionEntity> permissionEntityList = rolePermissionService.selectByRoleId(roleEntity.getId());
        if (CollectionUtil.isNotEmpty(permissionEntityList)) {
            List<Long> permissionIds = permissionEntityList.stream().map(RolePermissionEntity::getPermissionId).collect(Collectors.toList());
            List<PermissionVO> permissionVOS = permissionService.selectByIds(permissionIds);
            roleVo.setPermissionVOList(permissionVOS);
        }
        return roleVo;
    }

    @Override
    public List<RoleVO> getAll() {
        List<RoleEntity> roleEntityList = this.list();
        if (CollectionUtil.isNotEmpty(roleEntityList)) {
            List<RoleVO> roleVOS = roleEntityList.stream().map(RoleVO::converter).collect(Collectors.toList());
            List<Long> roleIds = roleEntityList.stream().map(RoleEntity::getId).collect(Collectors.toList());
            List<RolePermissionEntity> permissionEntityList = rolePermissionService.selectByRoleIds(roleIds);
            List<Long> permissionIds = permissionEntityList.stream().map(RolePermissionEntity::getPermissionId).collect(Collectors.toList());
            List<PermissionVO> permissionVOS = permissionService.selectByIds(permissionIds);
            Map<Long, List<RolePermissionEntity>> rolePerssionMap = permissionEntityList.stream().collect(Collectors.groupingBy(RolePermissionEntity::getRoleId));
            for (RoleVO roleVO : roleVOS) {
                List<RolePermissionEntity> permissionList = rolePerssionMap.get(roleVO.getId());
                if (CollectionUtil.isEmpty(permissionList)) continue;
                List<Long> permissionIdList = permissionList.stream().map(RolePermissionEntity::getPermissionId).collect(Collectors.toList());
                List<PermissionVO> permissionVOList = permissionVOS.stream().filter(vo -> permissionIdList.contains(vo.getId())).collect(Collectors.toList());
                roleVO.setPermissionVOList(permissionVOList);
            }
            return roleVOS;
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveRolePermission(RolePermissionVO rolePermissionVO) {
        Long roleId = rolePermissionVO.getRoleId();
        RoleEntity role = this.getById(roleId);
        if (role == null) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "角色不存在");
        }
        List<Long> permissionIds = rolePermissionVO.getPermissionIds();
        if (CollectionUtil.isEmpty(permissionIds)) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "权限不能为空");
        }
        rolePermissionService.removeByRole(roleId);
        List<RolePermissionEntity> rolePermissionEntityList = permissionIds.stream().map(permissionId -> {
            RolePermissionEntity rolePermissionEntity = new RolePermissionEntity();
            rolePermissionEntity.setRoleId(roleId);
            rolePermissionEntity.setPermissionId(permissionId);
            return rolePermissionEntity;
        }).collect(Collectors.toList());
        return rolePermissionService.saveBatch(rolePermissionEntityList);
    }

    @Override
    public RoleVO selectById(Long roleId) {
        RoleEntity roleEntity = this.getById(roleId);
        if (roleEntity == null) return null;
        RoleVO roleVo = RoleVO.converter(roleEntity);
        List<RolePermissionEntity> permissionEntityList = rolePermissionService.selectByRoleId(roleEntity.getId());
        if (CollectionUtil.isNotEmpty(permissionEntityList)) {
            List<Long> permissionIds = permissionEntityList.stream().map(RolePermissionEntity::getPermissionId).collect(Collectors.toList());
            List<PermissionVO> permissionVOS = permissionService.selectByIds(permissionIds);
            roleVo.setPermissionVOList(permissionVOS);
        }
        return roleVo;
    }

    @Override
    public List<RoleEntity> selectByIds(List<Long> roleIds) {
        if (CollectionUtil.isEmpty(roleIds)) return new ArrayList<>();
        LambdaQueryWrapper<RoleEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(RoleEntity::getId, roleIds);
        queryWrapper.select(RoleEntity::getId, RoleEntity::getRoleCode);
        return this.list(queryWrapper);
    }
}
