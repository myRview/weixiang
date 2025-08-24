package com.hk.task;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.hk.elasticsearch.dao.OperationLogESearchDao;
import com.hk.elasticsearch.vo.OperationLogEsVO;
import com.hk.service.log.OperationLogService;
import com.hk.vo.log.OperationLogVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author huangkun
 * @date 2025/8/24 16:07
 */
@Component
@Slf4j
public class OperatorLogSyncTask {

    @Autowired
    private OperationLogESearchDao operationLogESearchDao;
    @Autowired
    private OperationLogService operationLogService;

    /**
     * 同步操作日志数据到ES
     * 每1小时同步一次
     */
    @Scheduled(cron = "0 0 */1 * * ?")
    public void run() {
        Date recentDate = DateUtil.offsetHour(DateUtil.date(), -2);
        List<OperationLogVO> logVOList = operationLogService.selectAll(recentDate);
        if (CollectionUtil.isEmpty(logVOList)) return;
        List<OperationLogEsVO> esVOS = logVOList.parallelStream().map(OperationLogEsVO::convertToEsVO).collect(Collectors.toList());
        operationLogESearchDao.saveAll(esVOS);
        log.info("同步操作日志数据到ES完成，共{}条数据", esVOS.size());
    }
}
