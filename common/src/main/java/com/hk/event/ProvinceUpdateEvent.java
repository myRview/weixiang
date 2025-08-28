package com.hk.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;

/**
 * @author huangkun
 * @date 2025/8/28 22:38
 */
@Slf4j
public class ProvinceUpdateEvent extends ApplicationEvent {
    private Long provinceId;

    public ProvinceUpdateEvent(Object source, Long provinceId) {
        super(source);
        this.provinceId = provinceId;
    }

    public Long getProvinceId() {
        return provinceId;
    }
}
