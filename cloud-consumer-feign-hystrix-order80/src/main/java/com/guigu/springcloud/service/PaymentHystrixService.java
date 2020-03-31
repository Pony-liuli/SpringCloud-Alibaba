package com.guigu.springcloud.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient( value =  "CLOUD-PROVIDER-HYSTRIX-PAYMENT",fallback = PaymentFallbackService.class) //fallback 表示由PaymentFallbackService这个类处理异常
public interface PaymentHystrixService {

    @GetMapping(value = "/payment/hystrix/ok/{id}")
    String  paymentInfo(@PathVariable("id") Integer id);

    @GetMapping(value = "/payment/hystrix/timeout/{id}")
    String  paymentInfoTimeout(@PathVariable("id") Integer id);

}
