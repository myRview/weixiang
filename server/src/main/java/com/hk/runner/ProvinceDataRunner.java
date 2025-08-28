package com.hk.runner;

import com.hk.service.system.ProvinceService;
import com.hk.vo.system.ProvinceVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author huangkun
 * @date 2025/8/28 20:59
 */
//@Component
@Slf4j
public class ProvinceDataRunner implements CommandLineRunner {

    @Autowired
    private ProvinceService provinceService;


    @Override
    public void run(String... args) throws Exception {
        List<ProvinceVO> provinceVOS = provinceService.selectAll();
        log.info("初始化加载省份数据:{}", provinceVOS.size());
    }
}
