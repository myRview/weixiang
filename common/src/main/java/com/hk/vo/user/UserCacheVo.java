package com.hk.vo.user;

import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author huangkun
 * @date 2025/7/5 15:27
 */
@Data
public class UserCacheVo implements Serializable {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * token
     */
    private String token;

    /**
     * 用户信息
     */

    private UserVO user;

    /**
     * 登录时间
     */
    private Long loginTime;

    /**
     * 过期时间
     */
    private Long expireTime;

    private Set<String> permissions = new HashSet<>();

}
