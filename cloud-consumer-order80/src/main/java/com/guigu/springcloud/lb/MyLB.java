package com.guigu.springcloud.lb;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component // 让容器扫描到的内容
public class MyLB implements LoadBalancer{

    private AtomicInteger atomicInteger = new AtomicInteger(0);

    public final int getAndIncrement(){
        int current; //当前访问次数
        int next;  // 下一次访问次数

        do {

            current = this.atomicInteger.get();

            next = current >= 2147483647 ? 0 : current + 1;

        }while (!this.atomicInteger.compareAndSet(current,next));
        System.out.println("***第"+next+"次访问***");

        return next;
    }

    // 得到要访问的机器
    @Override
    public ServiceInstance instance(List<ServiceInstance> serviceInstances) {

        // 当前访问次数 取余 集群机器总数量 得到下一次访问的下标
        int index = getAndIncrement() % serviceInstances.size();

        // 根据下标取出机器 并返回
        return serviceInstances.get(index);
    }

}
