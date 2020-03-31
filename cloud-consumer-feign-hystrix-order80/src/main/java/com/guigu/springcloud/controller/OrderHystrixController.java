package com.guigu.springcloud.controller;

import com.guigu.springcloud.service.PaymentHystrixService;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import feign.Param;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
@DefaultProperties(defaultFallback = "paymentGlobalFallBackMethod") // 默认的全局配置方法 用来处理异常和超时
public class OrderHystrixController {

    @Resource
    private PaymentHystrixService paymentHystrixService;

    @GetMapping(value = "/consumer/payment/hystrix/ok/{id}")
    String  paymentInfo(@PathVariable("id") Integer id){

        String result = paymentHystrixService.paymentInfo(id);
        return result;
    }

//    @HystrixCommand(fallbackMethod = "paymentTimeoutHandler",commandProperties = {
//            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "1500")  //1.5秒钟以内就是正常的业务逻辑
//    })
    @GetMapping(value = "/consumer/payment/hystrix/timeout/{id}")
    @HystrixCommand // 表示该方法出异常由全局fallback方法处理
    String paymentInfoTimeout(@PathVariable("id") Integer id){

        int i=0/0;
        String  result = paymentHystrixService.paymentInfoTimeout(id);
        return result;
    }

    // 局部fallback方法 系统出错就调用该方法
    public String paymentTimeoutHandler(@PathVariable("id") Integer id){

        return "对方支付系统繁忙 请稍后再试 "+id;
    }

    // 全局fallback方法
    public String paymentGlobalFallBackMethod(){
        return  "全局异常处理信息,请稍后重试";
    }

}
