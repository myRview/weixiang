package com.hk.security.auth;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.hk.vo.user.UserCacheVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Set;

/**
 * @author huangkun
 * @date 2025/7/23 10:44
 */
@Component("ss")
@Slf4j
public class CheckPermissionService {
    public boolean hasPermission(String permission) {
        if (StrUtil.isBlank(permission)) return false;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserCacheVo userCacheVo = null;
        try {
            userCacheVo = (UserCacheVo) authentication.getPrincipal();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("获取用户信息失败:{}", e);
        }
        if (ObjUtil.isNull(userCacheVo) || CollectionUtils.isEmpty(userCacheVo.getPermissions())) {
            return false;
        }
        return hasPermissions(userCacheVo.getPermissions(), permission);
    }

    /**
     * 判断是否包含权限
     *
     * @param permissions 权限列表
     * @param permission  权限字符串
     * @return 用户是否具备某权限
     */
    private boolean hasPermissions(Set<String> permissions, String permission) {
        return permissions.contains(permission);
    }
}
