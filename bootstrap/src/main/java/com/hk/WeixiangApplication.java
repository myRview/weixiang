package com.hk;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author huangkun
 * @date 2025/6/22 9:14
 */
@SpringBootApplication
@MapperScan("com.hk.mapper")
public class WeixiangApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeixiangApplication.class, args);
    }
}
