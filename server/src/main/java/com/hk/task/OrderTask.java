package com.hk.task;

import cn.hutool.core.collection.CollectionUtil;
import com.hk.cache.RedisService;
import com.hk.constants.BaseConstant;
import com.hk.vo.order.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author huangkun
 * @date 2025/8/22 17:09
 */
@Component
@Slf4j
public class OrderTask {

    @Autowired
    private RedisService redisService;

    /**
     * 定时任务：每分钟更新一次订单有效时长（Cron表达式可根据需求调整）
     * Cron格式：秒 分 时 日 月 周 年（此处"0 0/1 * * * ?"表示每分钟执行一次）
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void run() {
        Map<String, OrderVO> orderVOMap = redisService.entriesHash(getHashKey());
        log.info("订单数据量：{}", orderVOMap.size());
        List<String> deleteKeys = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(orderVOMap)) {
            for (Map.Entry<String, OrderVO> entry : orderVOMap.entrySet()) {
                String key = entry.getKey();
                OrderVO orderVO = entry.getValue();
                log.info("key: {}, orderVO: {}", key, orderVO);
                Long validTime = orderVO.getValidTime();
                //更新有效时长
                validTime -= 3 * 60 * 1000;
                if (validTime <= 0) {
                    deleteKeys.add(key);
                }
                orderVO.setValidTime(validTime);
            }
            if (CollectionUtil.isNotEmpty(deleteKeys)){
                redisService.deleteHash(getHashKey(), deleteKeys.toArray(new String[0]));
            }
            redisService.putAllHash(getHashKey(), orderVOMap);
        }

    }

    private String getHashKey() {
        return String.format("%s:order", BaseConstant.CACHE_PREFIX);
    }
}
