package com.guigu.springcloud.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {


    // feign的详细日志
    @Bean
    Logger.Level feignLoggerLevel() {

        return Logger.Level.FULL;
    }
}
