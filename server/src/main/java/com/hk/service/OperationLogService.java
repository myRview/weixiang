package com.hk.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hk.entity.OperationLogEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hk.param.LogSearchParam;
import com.hk.vo.log.OperationLogVO;

/**
* <p>
    * 操作日志表 服务类
    * </p>
*
* @author hk
* @since 2025-06-22
*/
public interface OperationLogService extends IService<OperationLogEntity> {

    OperationLogVO getOperationLogById(Long id);

    Page<OperationLogVO> selectOperaLogPage(LogSearchParam searchParam);
}