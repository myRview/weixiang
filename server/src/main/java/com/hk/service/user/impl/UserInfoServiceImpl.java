package com.hk.service.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.entity.user.UserInfoEntity;
import com.hk.mapper.user.UserInfoMapper;
import com.hk.service.user.UserInfoService;
import com.hk.vo.user.UserExpandVo;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author hk
 * @since 2025-07-13
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfoEntity> implements UserInfoService {
    @Override
    public UserExpandVo getByUserId(Long userId) {
        LambdaQueryWrapper<UserInfoEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfoEntity::getUserId, userId);
        queryWrapper.last(" limit 1");
        queryWrapper.select(UserInfoEntity::getId, UserInfoEntity::getUserId, UserInfoEntity::getBirthday,
                UserInfoEntity::getProvince, UserInfoEntity::getCity, UserInfoEntity::getDistrict,
                UserInfoEntity::getAddress, UserInfoEntity::getBio
        );
        UserInfoEntity userInfo = this.getOne(queryWrapper);
        return UserExpandVo.convert(userInfo);
    }

    @Override
    public void deleteByUserId(Long userId) {
        this.remove(new LambdaQueryWrapper<UserInfoEntity>().eq(UserInfoEntity::getUserId, userId));
    }
}
