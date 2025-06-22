package com.hk.service.impl;

import com.hk.entity.UserEntity;
import com.hk.mapper.UserMapper;
import com.hk.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author hk
 * @since 2025-06-22
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {

}
