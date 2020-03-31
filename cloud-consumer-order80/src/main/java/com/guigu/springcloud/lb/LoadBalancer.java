package com.guigu.springcloud.lb;


import org.springframework.cloud.client.ServiceInstance;

import java.util.List;

public interface LoadBalancer {

    // 轮询算法 选出服务机器
    ServiceInstance instance(List<ServiceInstance> serviceInstances);


}
