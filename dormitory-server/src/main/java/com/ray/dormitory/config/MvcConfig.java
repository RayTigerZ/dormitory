package com.ray.dormitory.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @author Ray Z
 * @date 2019.10.26 20:55
 */
@Configuration
public class MvcConfig extends WebMvcConfigurationSupport {
    @Override
    //配置跨域
    protected void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                .allowCredentials(true);

    }
}
