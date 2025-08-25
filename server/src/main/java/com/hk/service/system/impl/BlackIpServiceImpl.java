package com.hk.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.entity.system.BlackIpEntity;
import com.hk.mapper.system.BlackIpMapper;
import com.hk.service.system.BlackIpService;
import com.hk.vo.system.BlackIpVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 黑名单ip表 服务实现类
 * </p>
 *
 * @author hk
 * @since 2025-08-25
 */
@Service
public class BlackIpServiceImpl extends ServiceImpl<BlackIpMapper, BlackIpEntity> implements BlackIpService {
    @Override
    public List<BlackIpVO> selectAll() {
        LambdaQueryWrapper<BlackIpEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(BlackIpEntity::getId);
        queryWrapper.select(BlackIpEntity::getId, BlackIpEntity::getIp, BlackIpEntity::getUserId);
        List<BlackIpEntity> list = this.list(queryWrapper);
        return list.stream().map(BlackIpVO::convertToVo).toList();
    }
}
