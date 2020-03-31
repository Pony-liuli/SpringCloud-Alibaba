package com.guigu.springcloud.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Slf4j
// 微服务的提供者
public class PaymentController {

    @Value("${server.port}")
    private String serverPort;

    @RequestMapping( value ="/payment/zk")
    public String paymentZk(){

        return "spring cloud with zookeeper: " +serverPort+"\t"+ UUID.randomUUID().toString();
    }
}
