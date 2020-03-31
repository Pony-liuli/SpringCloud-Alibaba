package com.guigu.springcloud.alibaba.service;

import com.guigu.springcloud.entities.CommonResult;
import com.guigu.springcloud.entities.Payment;
import org.springframework.stereotype.Component;

// 服务降级类 调用方法时出错了就调用下面的方法
@Component
public class PaymentFallbackService implements PaymentService{

    @Override
    public CommonResult<Payment> paymentSQL(Long id) {

        return new CommonResult<>(555,"服务降级返回---PaymentFallbackService",new Payment(id,"serialError"));
    }
}
