package com.atguigu.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@ComponentScan("com.mocha.jsrd.controller")
@Configuration
public class WebAppConfigurer implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**")
            //            .allowedOrigins("*")
            .allowedOriginPatterns("*")
            .allowCredentials(true)
            .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
            .maxAge(3600);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        String[] patterns = new String[]{"/manage/**", "/product/**", "/bigType/**", "/users/wxlogin",
            "/weixinpay/**"};
        registry.addInterceptor(new SysInterceptor())
            .addPathPatterns("/**")
            .excludePathPatterns(patterns);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/image/**")
            .addResourceLocations("file:/F:/imgs/");

    }

}
