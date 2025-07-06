package com.hk.service.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hk.entity.user.PermissionEntity;
import com.hk.param.PermissionSearchParam;
import com.hk.vo.user.PermissionVO;

import java.util.List;

/**
* <p>
    * 权限表 服务类
    * </p>
*
* @author hk
* @since 2025-06-30
*/
public interface PermissionService extends IService<PermissionEntity> {

    List<PermissionVO> selectByIds(List<Long> permissionIds);

    IPage<PermissionVO> getPermissionPage(PermissionSearchParam searchParam);

    List<PermissionVO> getPermissionList();
}