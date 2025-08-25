package com.hk.runner;

import cn.hutool.core.collection.CollectionUtil;
import com.hk.service.system.BlackIpService;
import com.hk.utils.BlackIpUtil;
import com.hk.vo.system.BlackIpVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author huangkun
 * @date 2025/8/25 14:45
 */
@Component
@Slf4j
public class BackIpRunner implements CommandLineRunner {

    @Autowired
    private BlackIpService blackIpService;

    @Override
    public void run(String... args) throws Exception {
        log.info("BackIpRunner run");
        List<BlackIpVO> blackIpVOS = blackIpService.selectAll();
        List<String> backIps =new ArrayList<>();
        if (CollectionUtil.isNotEmpty(blackIpVOS)){
            log.info("黑名单数据:{}", blackIpVOS);
             backIps = blackIpVOS.stream().map(BlackIpVO::getIp).collect(Collectors.toList());
        }
        // 初始化黑名单IP
        BlackIpUtil.initBackIp(backIps);
    }
}
