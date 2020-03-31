package com.guigu.springcloud.service;

import cn.hutool.core.util.IdUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.TimeUnit;

@Service
public class PaymentService {


    public String paymentInfo(Integer id){

        return "线程池: "+ Thread.currentThread().getName()+" paymentInfo_id " +id;
    }

    @HystrixCommand(fallbackMethod = "paymentTimeoutHandler",commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "3000")  //3秒钟以内就是正常的业务逻辑
    })
    public String paymentTimeout(Integer id){
        int timeout =13;
        // int i=0/0;
        try {
            TimeUnit.SECONDS.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "线程池: "+ Thread.currentThread().getName()+" paymentTimeout " +id+"\t" +"耗时"+timeout+"秒钟";
    }

    // 兜底方法 系统出错就调用该方法
    public String paymentTimeoutHandler(Integer id){

        return "线程池: "+ Thread.currentThread().getName()+" paymentTimeoutHandler " +id +"系统错误请稍后再试";
    }

    // 服务熔断
    @HystrixCommand(fallbackMethod = "paymentCircuitBreaker_fallback",commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enabled",value = "true"),  //是否开启断路器
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value = "10"),   //请求次数
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value = "10000"),  //时间范围 10秒
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value = "60"), //失败率达到多少后跳闸 10秒内请求10次失败率达到60%直接跳闸
    })
    // 断路器方法
    public String paymentCircuitBreaker(@PathVariable("id") Integer id){
        if (id < 0){
            throw new RuntimeException("*****id 不能负数");
        }
        String serialNumber = IdUtil.simpleUUID(); // 随机生成一个流水号

        return Thread.currentThread().getName()+"\t"+"调用成功,流水号："+serialNumber;
    }

    // fallback方法
    public String paymentCircuitBreaker_fallback(@PathVariable("id") Integer id){
        return "id 不能负数，请稍候再试,(┬＿┬)/~~     id: " +id;
    }
}
