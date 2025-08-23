package com.hk.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hk.entity.user.UserInfoEntity;
import com.hk.vo.user.UserExpandVo;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author hk
 * @since 2025-07-13
 */
public interface UserInfoService extends IService<UserInfoEntity> {

    UserExpandVo getByUserId(Long userId);

    void deleteByUserId(Long userId);
}
