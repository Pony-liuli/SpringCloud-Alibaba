package com.guigu.springcloud.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@RestController
@Slf4j

// 微服务的消费者
public class OrderZKController {

    // 定义微服务URL地址
    public static  final String INVOKE_URL = "http://cloud-provide-payment";

    @Resource
    private RestTemplate restTemplate;

    @GetMapping(value = "/consumer/payment/zk")
    public String paymentInfo(){

        // 传入一个class解析 并生成
        String  result= restTemplate.getForObject(INVOKE_URL+"/payment/zk",String.class);
        return result;
    }


}

