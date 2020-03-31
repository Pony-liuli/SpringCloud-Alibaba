package com.guigu.springcloud.alibaba.myhandle;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.guigu.springcloud.entities.CommonResult;

// 自定义全局处理方法

public class CustomerBlockHandler {

    public static CommonResult handlerException1 (BlockException exception){
        return new CommonResult(444, "自定义全局方法 handlerException1，global");
    }

    public static CommonResult handlerException2 (BlockException exception){
        return new CommonResult(444, "自定义全局方法 handlerException2，global");
    }
}
