package com.guigu.springcloud.service;


import org.springframework.stereotype.Component;

// 改类用于处理服务端发送宕机异常
@Component
public class PaymentFallbackService implements  PaymentHystrixService{
    @Override
    public String paymentInfo(Integer id) {
        return "PaymentFallbackService---paymentInfo, fall back";
    }

    @Override
    public String paymentInfoTimeout(Integer id) {
        return "PaymentFallbackService---paymentInfoTimeout, fall back";
    }
}
