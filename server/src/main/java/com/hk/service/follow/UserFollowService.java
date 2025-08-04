package com.hk.service.follow;

import com.hk.cache.RedisService;
import com.hk.constants.BaseConstant;
import com.hk.context.UserContext;
import com.hk.service.user.UserService;
import com.hk.vo.user.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @author huangkun
 * @date 2025/8/4 14:56
 */
@Service
public class UserFollowService {

    @Autowired
    private RedisService<Long> redisService;
    @Autowired
    private UserService userService;

    public Boolean changStatus(Long userId, Integer status) {
        Long currentUserId = UserContext.getCurrentUserId();
        if (currentUserId == null) {
            return false;
        }
        if (status == 1) {
            redisService.addSet(getFollowKey(currentUserId), userId);
            redisService.addSet(getFansKey(userId), currentUserId);
        } else {
            redisService.removeSet(getFollowKey(currentUserId), userId);
            redisService.removeSet(getFansKey(userId), currentUserId);
        }
        return true;
    }

    public List<UserVO>  list(Long userId) {
        Set<Long> set = redisService.membersSet(getFollowKey(userId));
        List<UserVO> userVOS = userService.selectByIds(set);
        return userVOS;
    }

    public List<UserVO> fansList(Long userId) {
        Set<Long> set = redisService.membersSet(getFansKey(userId));
        List<UserVO> userVOS = userService.selectByIds(set);
        return userVOS;
    }

    public Boolean status(Long userId) {
        Long currentUserId = UserContext.getCurrentUserId();
        if (currentUserId == null) {
            return false;
        }
        return redisService.isMemberSet(getFollowKey(currentUserId), userId);
    }

    private String getFollowKey(Long userId) {
        return String.format("%s:%s:%s", BaseConstant.CACHE_PREFIX, "follow", userId);
    }

    private String getFansKey(Long userId) {
        return String.format("%s:%s:%s", BaseConstant.CACHE_PREFIX, "fans", userId);
    }
}
