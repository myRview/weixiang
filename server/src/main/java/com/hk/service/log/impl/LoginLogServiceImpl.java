package com.hk.service.log.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.entity.log.LoginLogEntity;
import com.hk.entity.log.OperationLogEntity;
import com.hk.mapper.log.LoginLogMapper;
import com.hk.param.LogSearchParam;
import com.hk.service.log.LoginLogService;
import com.hk.vo.log.LoginLogVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    @Override
    public IPage<LoginLogVO> selectLoginPage(LogSearchParam searchParam) {
        String username = searchParam.getUsername();
        String ipAddress = searchParam.getIpAddress();
        Integer pageNum = searchParam.getPageNum();
        Integer pageSize = searchParam.getPageSize();

        LambdaQueryWrapper<LoginLogEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(username), LoginLogEntity::getUsername, username);
        queryWrapper.like(StringUtils.isNotBlank(ipAddress), LoginLogEntity::getIpAddress, ipAddress);
        queryWrapper.orderByDesc(LoginLogEntity::getId);
        queryWrapper.select(LoginLogEntity::getId,
                LoginLogEntity::getUsername,
                LoginLogEntity::getIpAddress,
                LoginLogEntity::getLocation,
                LoginLogEntity::getDevice,
                LoginLogEntity::getLoginTime,
                LoginLogEntity::getStatus
        );
        Page<LoginLogEntity> page = this.page(new Page<>(pageNum, pageSize), queryWrapper);
        Page<LoginLogVO> pageResult = new Page<>(pageNum, pageSize,page.getTotal());
        List<LoginLogEntity> records = page.getRecords();
        if (CollectionUtil.isNotEmpty(records)){
            pageResult.setRecords(records.stream().map(LoginLogVO::converter).collect(Collectors.toList()));
        }
        return pageResult;
    }

    @Override
    public LoginLogVO getInfoById(Long id) {
        LoginLogEntity loginLogEntity = this.getById(id);
        if (loginLogEntity != null){
            return LoginLogVO.converter(loginLogEntity);
        }
        return null;
    }
}
