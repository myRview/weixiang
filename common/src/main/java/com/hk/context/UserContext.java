package com.hk.context;

import com.hk.enums.UserRoleEnum;
import com.hk.manager.TokenManager;
import com.hk.vo.user.UserCacheVo;
import com.hk.vo.user.UserVO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class UserContext {

    private static TokenManager tokenManager;

    public UserContext(TokenManager tokenManager) {
        UserContext.tokenManager = tokenManager;
    }

    /**
     * 获取当前请求对象
     */
    public static HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attributes.getRequest();
    }

    /**
     * 获取当前用户缓存信息
     */
    public static UserCacheVo getCurrentUser() {
        return tokenManager.getLoginUser(getCurrentRequest());
    }

    /**
     * 获取当前用户ID
     */
    public static Long getCurrentUserId() {
        UserCacheVo user = getCurrentUser();
        return user != null ? user.getUserId() : null;
    }

    /**
     * 获取当前用户名
     */
    public static String getCurrentUsername() {
        UserCacheVo user = getCurrentUser();
        return (user != null && user.getUser() != null) ? user.getUser().getUserName() : "anonymous";
    }

    public static boolean isAdmin() {
        UserCacheVo user = getCurrentUser();
        if (user != null) {
            UserVO userVO = user.getUser();
            return UserRoleEnum.ADMIN.getValue().equals(userVO.getRoleCode());
        }
        return false;
    }

    /**
     * 获取当前用户令牌
     */
    public static String getCurrentToken() {
        UserCacheVo user = getCurrentUser();
        return (user != null) ? user.getToken() : null;
    }

    /**
     * 检查当前用户是否已认证
     */
    public static boolean isAuthenticated() {
        return getCurrentUser() != null;
    }
}