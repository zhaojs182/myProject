package com.selfstudy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class MebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 配置哪些路径支持跨域
                .allowedOrigins("*", "*")  // 允许的源地址
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // 允许的请求方法
                .allowedHeaders("*")  // 允许的请求头
                .allowCredentials(false)  // 是否支持凭证，如 Cookies 或 Authorization 头部
                .maxAge(3600);  // 预检请求的缓存时间，单位秒
    }
}
