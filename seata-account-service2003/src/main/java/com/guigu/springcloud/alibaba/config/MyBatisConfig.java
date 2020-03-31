package com.guigu.springcloud.alibaba.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan({"com.guigu.springcloud.alibaba.dao"})
public class MyBatisConfig {

}