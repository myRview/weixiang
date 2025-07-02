package com.hk;


import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

public class CodeGenerator {

    public static void main(String[] args) {
        String projectPath = System.getProperty("user.dir");
        String outputDir = projectPath + "/src/main/java";
        String xmlPath = projectPath + "/src/main/resources/mapper";

        FastAutoGenerator.create("jdbc:mysql://192.168.159.128:3306/Weixiang_Resource?useSSL=false&serverTimezone=UTC", "root", "123456") // 替换为实际密码
                .globalConfig(builder -> {
                    builder.author("hk") // 设置作者
//                            .enableSwagger() // 开启 swagger 模式
                            .outputDir(outputDir); // 指定输出目录
                }).packageConfig(builder -> {
                    builder.parent("com.hk") // 设置父包名
                            .entity("entity") // 实体类包名
                            .service("service") // service包名
                            .serviceImpl("service.impl") // service实现类包名
                            .mapper("mapper") // mapper包名
                            .xml("mapper") // xml文件包名
                            .controller("controller") // controller包名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, xmlPath)); // 设置mapperXml生成路径
                }).strategyConfig(builder -> {
                    // 设置需要生成的表名
                    builder.addInclude("member_plan", "order_info", "sys_login_log", "sys_operation_log", "sys_user").addTablePrefix("sys_"); // 设置过滤表前缀

                    // Entity 策略配置
                    builder.entityBuilder().enableLombok() // 开启Lombok
                            .enableTableFieldAnnotation() // 开启字段注解
                            .formatFileName("%sEntity"); // 实体命名格式

                    // Service 策略配置
                    builder.serviceBuilder().formatServiceFileName("%sService") // service接口命名格式
                            .formatServiceImplFileName("%sServiceImpl"); // service实现类命名格式

                    // Controller 策略配置
                    builder.controllerBuilder().enableRestStyle(); // 开启生成@RestController控制器

                    // Mapper 策略配置
                    builder.mapperBuilder().enableBaseResultMap() // 启用BaseResultMap
                            .enableBaseColumnList() // 启用BaseColumnList
                            .formatMapperFileName("%sMapper") // mapper接口命名格式
                            .formatXmlFileName("%sMapper"); // xml文件命名格式
                }).templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板
                .execute();
    }
}