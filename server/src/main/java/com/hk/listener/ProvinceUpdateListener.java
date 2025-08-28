package com.hk.listener;

import com.alibaba.fastjson.JSONObject;
import com.hk.event.ProvinceUpdateEvent;
import com.hk.service.system.ProvinceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author huangkun
 * @date 2025/8/28 22:53
 */
@Slf4j
@Component
public class ProvinceUpdateListener implements ApplicationListener<ProvinceUpdateEvent> {

    @Autowired
    private ProvinceService provinceService;

    @Override
    public void onApplicationEvent(ProvinceUpdateEvent event) {
        log.info("监听事件，事件数据：{}", JSONObject.toJSONString(event));
        provinceService.saveToRedis(event.getProvinceId());
    }
}
