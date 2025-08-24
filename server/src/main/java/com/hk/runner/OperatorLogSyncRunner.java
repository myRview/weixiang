package com.hk.runner;

import cn.hutool.core.collection.CollectionUtil;
import com.hk.elasticsearch.dao.OperationLogESearchDao;
import com.hk.elasticsearch.vo.OperationLogEsVO;
import com.hk.service.log.OperationLogService;
import com.hk.vo.log.OperationLogVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 操作日志同步到ES
 * @author huangkun
 * @date 2025/8/24 16:02
 */
//@Component
@Slf4j
public class OperatorLogSyncRunner implements CommandLineRunner {

    @Autowired
    private OperationLogESearchDao operationLogESearchDao;
    @Autowired
    private OperationLogService operationLogService;

    @Override
    public void run(String... args) throws Exception {
        List<OperationLogVO> logVOList = operationLogService.selectAll(null);
        if (CollectionUtil.isEmpty(logVOList)) return;
        List<OperationLogEsVO> esVOS = logVOList.parallelStream().map(OperationLogEsVO::convertToEsVO).collect(Collectors.toList());
        operationLogESearchDao.saveAll(esVOS);
        log.info("同步操作日志数据到ES完成，共{}条数据", esVOS.size());
    }
}
