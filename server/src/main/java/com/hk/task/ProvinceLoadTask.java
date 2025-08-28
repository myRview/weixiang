package com.hk.task;

import com.hk.service.system.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author huangkun
 * @date 2025/8/28 21:13
 */
@Component
public class ProvinceLoadTask {
    @Autowired
    private ProvinceService provinceService;

    /**
     * 加载省份信息到redis，每29天同步一次
     */
    @Scheduled(cron = "0 0 0/29 * * ?")
    public void loadProvince() {
        provinceService.selectAll();
    }
}
