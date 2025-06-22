package com.hk.service.impl;

import com.hk.entity.OperationLogEntity;
import com.hk.mapper.OperationLogMapper;
import com.hk.service.OperationLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
