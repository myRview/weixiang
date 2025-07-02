package com.hk.mapper.log;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hk.entity.log.OperationLogEntity;

import java.util.List;

/**
 * <p>
 * 操作日志表 Mapper 接口
 * </p>
 *
 * @author hk
 * @since 2025-06-22
 */
public interface OperationLogMapper extends BaseMapper<OperationLogEntity> {

    long countByCondition(String username, String ipAddress);

    List<OperationLogEntity> selectPageByCondition(String username, String ipAddress, long offset, Integer pageSize);
}
