package com.hk;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author huangkun
 * @date 2025/6/22 9:14
 */
@SpringBootApplication
@MapperScan("com.hk.mapper")
@EnableRabbit
@EnableScheduling
@ServletComponentScan
public class WeixiangApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeixiangApplication.class, args);
    }
}
