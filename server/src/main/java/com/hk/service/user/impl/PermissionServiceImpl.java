package com.hk.service.user.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.entity.user.PermissionEntity;
import com.hk.mapper.user.PermissionMapper;
import com.hk.param.PermissionSearchParam;
import com.hk.service.user.PermissionService;
import com.hk.vo.user.PermissionVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 权限表 服务实现类
 * </p>
 *
 * @author hk
 * @since 2025-06-30
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, PermissionEntity> implements PermissionService {
    @Override
    public List<PermissionVO> selectByIds(List<Long> permissionIds) {
        LambdaQueryWrapper<PermissionEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(PermissionEntity::getId, PermissionEntity::getPermissionName, PermissionEntity::getPermissionCode);
        if (CollectionUtil.isNotEmpty(permissionIds)) {
            queryWrapper.in(PermissionEntity::getId, permissionIds);
        }
        List<PermissionEntity> list = this.list(queryWrapper);
        return list.stream().map(PermissionVO::converter).collect(Collectors.toList());
    }

    @Override
    public IPage<PermissionVO> getPermissionList(PermissionSearchParam searchParam) {
        LambdaQueryWrapper<PermissionEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(PermissionEntity::getId, PermissionEntity::getPermissionName, PermissionEntity::getPermissionCode);
        Page<PermissionEntity> page = this.page(new Page<>(searchParam.getPageNum(), searchParam.getPageSize()), queryWrapper);
        IPage<PermissionVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        if (CollectionUtil.isNotEmpty(page.getRecords())) {
            voPage.setRecords(page.getRecords().stream().map(PermissionVO::converter).collect(Collectors.toList()));
        }
        return voPage;
    }
}
