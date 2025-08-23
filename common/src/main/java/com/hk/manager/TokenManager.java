package com.hk.manager;

import cn.hutool.core.util.StrUtil;
import com.hk.cache.RedisService;
import com.hk.constants.BaseConstant;
import com.hk.utils.JwtUtil;
import com.hk.vo.user.UserCacheVo;
import com.hk.vo.user.UserVO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author huangkun
 * @date 2025/7/5 15:23
 */
@Component
@Slf4j
public class TokenManager {


    private static final long MILLIS_MINUTE = 60 * 1000L;
    private static final long MILLIS_MINUTE_TEN = 60 * 60 * 1000L;

    @Value("${weixiang.token.expireTime}")
    private int expireTime = 30;
    @Autowired
    private RedisService redisService;

    /**
     * 获取用户信息
     *
     * @param request
     * @return
     */
    public UserCacheVo getLoginUser(HttpServletRequest request) {
        String token = request.getHeader(BaseConstant.TOKEN);
        if (StrUtil.isNotBlank(token)) {
            try {
                String userId = JwtUtil.getUserId(token);
                UserCacheVo userDetailVO = (UserCacheVo) redisService.getHash(getHashKey(), userId);
                return userDetailVO;
            } catch (Exception e) {
                log.error("获取用户信息异常:{}", e.getMessage());
                return null;
            }
        }
        return null;
    }

    /**
     * 创建令牌
     *
     * @param userVO        登录用户
     * @param permissionSet
     * @return
     */
    public String createToken(UserVO userVO, Set<String> permissionSet) {
        Map<String, Object> map = new HashMap<>();
        Long userId = userVO.getId();
        map.put(BaseConstant.USER_ID, userId);
        String token = JwtUtil.createToken(map);
        UserCacheVo userCacheVo = new UserCacheVo();
        userCacheVo.setUser(userVO);
        userCacheVo.setUserId(userId);
        userCacheVo.setToken(token);
        userCacheVo.setPermissions(permissionSet);
        refreshToken(userCacheVo);
        return token;
    }

    public void refreshToken(UserCacheVo userCacheVo) {
        String token = userCacheVo.getToken();
        Long userId = userCacheVo.getUserId();
        redisService.setWithExpire(getKey(userId), token, expireTime, TimeUnit.MINUTES);
        long time = System.currentTimeMillis();
        userCacheVo.setLoginTime(time);
        userCacheVo.setExpireTime(time + expireTime * MILLIS_MINUTE);
        redisService.putHash(getHashKey(), userId.toString(), userCacheVo);
        redisService.expire(getHashKey(), expireTime, TimeUnit.MINUTES);
    }

    private static String getKey(Long userId) {
        return BaseConstant.CACHE_PREFIX + BaseConstant.TOKEN + ":" + userId;
    }

    private static String getHashKey() {
        final String LOGIN_USER = "login_user";
        return BaseConstant.CACHE_PREFIX + LOGIN_USER;
    }

    /**
     * 验证令牌有效期，相差不足60分钟，自动刷新缓存
     *
     * @param loginUser
     * @return 令牌
     */
    public void verifyToken(UserCacheVo loginUser) {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= MILLIS_MINUTE_TEN) {
            refreshToken(loginUser);
        }
    }


    /**
     * 删除用户缓存信息
     *
     * @param userId 用户ID
     */
    public void delLoginUser(Long userId) {
        if (userId != null) {
            redisService.delete(getKey(userId));
            redisService.deleteHash(getHashKey(), userId.toString());
        }
    }

    public UserVO getUserInfo(Long userId) {
        Object userObj = redisService.getHash(getHashKey(), userId.toString());
        if (userObj != null) {
            return ((UserCacheVo) userObj).getUser();
        }
        return null;
    }

    public void updateCache(UserVO userVO) {
        UserCacheVo userCacheVo = (UserCacheVo) redisService.getHash(getHashKey(), userVO.getId().toString());
        if (userCacheVo != null) {
            userCacheVo.setUser(userVO);
            redisService.putHash(getHashKey(), userVO.getId().toString(), userCacheVo);
        }
    }
}
