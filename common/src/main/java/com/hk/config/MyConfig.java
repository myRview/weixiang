package com.hk.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.hk.interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author huangkun
 * @date 2025/6/27 16:04
 */
@Configuration
public class MyConfig implements WebMvcConfigurer {
    /**
     * 配置跨域
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("*")
                .allowCredentials(true)
                .allowedHeaders("*")
                .exposedHeaders("*");

    }

    /**
     * Mybatis分页配置
     *
     * @return
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

//    @Autowired
//    private TokenInterceptor tokenInterceptor;

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        String[] EXCLUDE_PATHS = {
//                "/api/swagger-ui/**",
//                "/api/swagger-resources/**",
//                "/api/v2/api-docs",
//                "/api/v3/api-docs/**",
//                "/api/webjars/**",
//                "/api/doc.html",
//                "/api/favicon.ico"
//        };
//
//        registry.addInterceptor(tokenInterceptor)
//                .addPathPatterns("/api/**")
//                .excludePathPatterns("/api/login")
//                .excludePathPatterns("/api/register")
//                .excludePathPatterns(EXCLUDE_PATHS);
//    }
}
