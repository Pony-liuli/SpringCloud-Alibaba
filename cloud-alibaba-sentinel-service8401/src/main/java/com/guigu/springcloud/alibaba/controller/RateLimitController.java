package com.guigu.springcloud.alibaba.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.guigu.springcloud.alibaba.myhandle.CustomerBlockHandler;
import com.guigu.springcloud.entities.CommonResult;
import com.guigu.springcloud.entities.Payment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RateLimitController {

    // 资源名限流
    @GetMapping("/byResource")
    @SentinelResource(value = "byResource", blockHandler = "handleException")
    public CommonResult byResource() {

        return new CommonResult(200, "按资源名限流OK", new Payment(2020L, "serial001"));
    }

    public CommonResult handleException(BlockException exception) {
        return new CommonResult(444, exception.getClass().getCanonicalName() + "\t 服务不可用");
    }

    // URL限流 自己写了就用自己的 不写就用默认的处理方法
    @GetMapping("/rateLimit/byUrl")
    @SentinelResource(value = "byUrl")
    public CommonResult byUrl() {
        return new CommonResult(200, "按url限流测试OK", new Payment(2020L, "serial002"));
    }


    // 按自定义限流测试
    @GetMapping("/rateLimit/customerBlockHandler")
    @SentinelResource(value = "customerBlockHandler",blockHandlerClass = CustomerBlockHandler.class,blockHandler = "handlerException2") //blockHandlerClass类里面的 handlerException2 方法为我们处理
    public CommonResult customerBlockHandler() {
        return new CommonResult(200, "按自定义限流测试OK", new Payment(2020L, "serial003"));
    }

}
