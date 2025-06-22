package com.hk.service.impl;

import com.hk.entity.LoginLogEntity;
import com.hk.mapper.LoginLogMapper;
import com.hk.service.LoginLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 登录日志表 服务实现类
 * </p>
 *
 * @author hk
 * @since 2025-06-22
 */
@Service
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLogEntity> implements LoginLogService {

}
