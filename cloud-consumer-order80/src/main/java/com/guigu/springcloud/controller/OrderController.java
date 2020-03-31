package com.guigu.springcloud.controller;

import com.guigu.springcloud.entities.CommonResult;
import com.guigu.springcloud.entities.Payment;
import com.guigu.springcloud.lb.LoadBalancer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.net.URI;
import java.util.List;

@RestController
@Slf4j
public class OrderController {

    // 单机版
    //public static final String PAYMENT_RUL = "http://localhost:8001";

    // 集群不能写死 写微服务的名称
    public static final String PAYMENT_RUL = "http://CLOUD-PAYMENT-SERVICE";

    @Resource
    private DiscoveryClient discoveryClient; // 获取Eureka注册成功的微服务信息

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private LoadBalancer loadBalancer;


    @GetMapping("/consumer/payment/create")
    public CommonResult<Payment> create(Payment payment){

        return restTemplate.postForObject(PAYMENT_RUL+"payment/create",payment,CommonResult.class);
    }

    // 返回对象为响应体重数据转化成的对象 基本上可以理解为json
    @GetMapping("/consumer/payment/get/{id}")
    public CommonResult<Payment> getPayment(@PathVariable("id") Long id){

        return restTemplate.getForObject(PAYMENT_RUL+"/payment/get/"+id,CommonResult.class);
    }

    @GetMapping(value = "/consumer/payment/discovery")
    public Object discovery(){

        List<String> services = discoveryClient.getServices();
        for (String str: services){
            log.info("*******element: *******"+str);
        }

        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        for (ServiceInstance instance : instances ){
            log.info(instance.getServiceId()+"\t"+instance.getHost()+"\t"+instance.getPort()+"\t"+instance.getUri());
        }

        return this.discoveryClient;
    }

    // 返回ResponseEntity对象 包含了响应中的一些重要信息 比如响应头 响应状态码 响应体
    @GetMapping("/consumer/payment/getForEntity/{id}")
    public CommonResult<Payment> getPayment2(@PathVariable("id") Long id){

        ResponseEntity<CommonResult> entity = restTemplate.getForEntity(PAYMENT_RUL+"/payment/get/"+id,CommonResult.class);
        // 判读请求是否成功
        if(entity.getStatusCode().is2xxSuccessful()){
            log.info(entity.getStatusCode()+"\t"+entity);
            return  entity.getBody();
        }else{
            return new CommonResult<>(444,"操作失败");
        }
    }

    // 使用自己写的负载均衡
    @GetMapping(value = "/consumer/payment/lb")
    public String getPaymentLB(){

        // 得到服务list
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");

        // 如果服务为null 或者大于等于0 就返回null
        if(instances ==null || instances.size() <=0){
            return null;
        }

        // 调用自己写的方法 将list服务数组传进去进行筛选
        ServiceInstance serviceInstance = loadBalancer.instance(instances);

        URI uri = serviceInstance.getUri();

        // 返回json信息
        return restTemplate.getForObject(uri+"/payment/lb",String.class);
    }

    // ====================> zipkin+sleuth
    @GetMapping("/consumer/payment/zipkin")
    public String paymentZipkin()
    {
        String result = restTemplate.getForObject("http://localhost:8001"+"/payment/zipkin/", String.class);
        return result;
    }

}
