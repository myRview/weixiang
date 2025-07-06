package com.hk.service.log;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hk.entity.log.OperationLogEntity;
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

    IPage<OperationLogVO> selectOperaLogPage(LogSearchParam searchParam);

    boolean addLog(OperationLogVO operationLog);
}