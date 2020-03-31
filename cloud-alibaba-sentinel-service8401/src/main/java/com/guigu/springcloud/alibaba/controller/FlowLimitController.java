package com.guigu.springcloud.alibaba.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;


@RestController
@Slf4j
public class FlowLimitController {
    @GetMapping("/testA")
    public String testA() {
        // 暂停
//        try {
//            TimeUnit.MILLISECONDS.sleep(800);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return "------testA";
    }

    @GetMapping("/testB")
    public String testB() {

        log.info(Thread.currentThread().getName()+"\t"+"...testB");
        return "------testB";
    }


    @GetMapping("/testD")
    public String testD()
    {
        log.info("testD 异常比例");
        int i=0/0;
        //log.info("testD 测试RT");
        // 线程暂停1秒
       // try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
        return "------testD";
    }

    @GetMapping("/testE")
    public String testE()
    {
        log.info("testE 测试异常数");
        int age = 10/0;
        return "------testE 测试异常数";
    }

    // 热点方法1
    @GetMapping("/testHotKey1")
    @SentinelResource(value = "testHotKey1") // @SentinelResource 名称要唯一 testHotKey要访问资源名 如果没有配置blockHandler 异常将会打到了前台用户界面看不到，不友好
    public String testHotKey1(@RequestParam(value = "p1",required = false) String p1, @RequestParam(value = "p2",required = false) String p2){

        return "----testHotKey1";
    }

    // 热点方法2
    @GetMapping("/testHotKey")
    @SentinelResource(value = "testHotKey", blockHandler = "deal_testHotKey") // @SentinelResource value 要访问资源名，称要唯一  blockHandler 操作违反条件时要调用的方法名
    public String testHotKey(@RequestParam(value = "p1",required = false) String p1, @RequestParam(value = "p2",required = false) String p2){

        int i=1/0;
        return "----testHotKey";
    }

    // 兜底方法 出错就调用该方法
    public String deal_testHotKey(String p1 , String  p2,BlockException blockException){

        return "deal_testHotKey 出错了请稍后再试"; // Sentinel 的默认提示：Blocked by Sentinel (flow limiting)
    }
}