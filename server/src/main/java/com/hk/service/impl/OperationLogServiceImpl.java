package com.hk.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hk.entity.OperationLogEntity;
import com.hk.entity.OperationLogEntity;
import com.hk.mapper.OperationLogMapper;
import com.hk.param.LogSearchParam;
import com.hk.service.OperationLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hk.vo.log.LoginLogVO;
import com.hk.vo.log.OperationLogVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 操作日志表 服务实现类
 * </p>
 *
 * @author hk
 * @since 2025-06-22
 */
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLogEntity> implements OperationLogService {
    @Override
    public OperationLogVO getOperationLogById(Long id) {
        OperationLogEntity operationLog = this.getById(id);
        if (operationLog != null) {
            return OperationLogVO.converter(operationLog);
        }
        return null;
    }

    @Override
    public Page<OperationLogVO> selectOperaLogPage(LogSearchParam searchParam) {
        String username = searchParam.getUsername();
        String ipAddress = searchParam.getIpAddress();
        Integer pageNum = searchParam.getPageNum();
        Integer pageSize = searchParam.getPageSize();

        LambdaQueryWrapper<OperationLogEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(username), OperationLogEntity::getUsername, username);
        queryWrapper.like(StringUtils.isNotBlank(ipAddress), OperationLogEntity::getIpAddress, ipAddress);
        queryWrapper.select(OperationLogEntity::getId,
                OperationLogEntity::getUsername,
                OperationLogEntity::getIpAddress,
                OperationLogEntity::getOperationAddress,
                OperationLogEntity::getOperationModule,
                OperationLogEntity::getOperationContent,
                OperationLogEntity::getOperationTime,
                OperationLogEntity::getUserId,
                OperationLogEntity::getStatus
        );
        Page<OperationLogEntity> page = this.page(new Page<>(pageNum, pageSize), queryWrapper);
        Page<OperationLogVO> pageResult = new Page<>(pageNum, pageSize, page.getTotal());
        List<OperationLogEntity> records = page.getRecords();
        if (CollectionUtil.isNotEmpty(records)) {
            pageResult.setRecords(records.stream().map(OperationLogVO::converter).collect(Collectors.toList()));
        }
        return pageResult;
    }
}
