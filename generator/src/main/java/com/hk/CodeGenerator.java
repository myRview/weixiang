package com.hk;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

public class CodeGenerator {

    public static void main(String[] args) {
        String projectPath = System.getProperty("user.dir");
        String outputDir = projectPath + "/src/main/java";
        String xmlPath = projectPath + "/src/main/resources/mapper";

        // 配置数据源
        String url = "jdbc:mysql://192.168.159.128:3306/Weixiang_Resource?useSSL=false&serverTimezone=UTC";
        String username = "root";
        String password = "123456";

        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> {
                    builder.author("hk") // 作者
                            .dateType(DateType.ONLY_DATE) // 日期类型
                            .outputDir(outputDir) // 输出目录
                            .enableSwagger() // 开启Swagger注解
                            .disableOpenDir(); // 禁止打开输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.hk") // 父包名
                            .entity("entity") // 实体类包名
                            .service("service") // Service包名
                            .serviceImpl("service.impl") // Service实现包名
                            .mapper("mapper") // Mapper包名
                            .controller("controller") // Controller包名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, xmlPath)); // XML位置
                })
                .strategyConfig(builder -> {
                    builder.addInclude("member_plan")
                            .addTablePrefix("sys_") // 表前缀过滤

                            // 实体类配置
                            .entityBuilder()
                            .enableLombok() // 启用Lombok
                            .enableTableFieldAnnotation() // 字段注解
                            .formatFileName("%sEntity") // 文件名称格式

                            // Service配置
                            .serviceBuilder()
                            .formatServiceFileName("%sService") // 接口名称
                            .formatServiceImplFileName("%sServiceImpl") // 实现类名称

                            // Controller配置
                            .controllerBuilder()
                            .enableRestStyle() // REST风格
                            .enableHyphenStyle() // 驼峰转连字符
                            .formatFileName("%sController") // 文件名称

                            // Mapper配置
                            .mapperBuilder()
                            .enableBaseResultMap() // 生成resultMap
                            .enableBaseColumnList() // 生成columnList
                            .formatMapperFileName("%sMapper") // 接口名称
                            .formatXmlFileName("%sMapper"); // XML名称
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker
                .execute();
    }
}