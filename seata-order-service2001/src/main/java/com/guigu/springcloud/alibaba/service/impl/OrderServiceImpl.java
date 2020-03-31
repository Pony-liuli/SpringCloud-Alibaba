package com.guigu.springcloud.alibaba.service.impl;

import com.guigu.springcloud.alibaba.dao.OrderDao;
import com.guigu.springcloud.alibaba.domain.Order;
import com.guigu.springcloud.alibaba.service.AccountService;
import com.guigu.springcloud.alibaba.service.OrderService;
import com.guigu.springcloud.alibaba.service.StorageService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderDao orderDao;

    @Resource
    private AccountService accountService;
    @Resource
    private StorageService storageService;

    @Override
    @GlobalTransactional(name="create-order",rollbackFor = Exception.class) // name 随便起，要唯一 rollbackFor 遇到了这个异常就回滚
    public void create(Order order) {

        System.out.println(order);
        log.info("-----> 开始新建订单");
        // 1. 新建订单
        orderDao.create(order);
        log.info("-----> 结束新建订单");

        log.info("-----> 订单微服务调用库存微服务方法，扣减商品数量开始");
        // 2.扣减库存
        storageService.decrease(order.getProductId(),order.getCount());
        log.info("-----> 订单微服务调用库存微服务方法，扣减商品数量结束");

        log.info("-----> 订单微服务调用账户微服务方法，扣除余额开始");
        // 3.扣除余额
        accountService.decrease(order.getUserId(),order.getMoney());
        log.info("-----> 订单微服务调用账户微服务方法，扣除余额结束");

        log.info("-----> 修改订单状态开始");
        // 4.修改订单的状态，从 0 到1 1代表已完成
        orderDao.update(order.getUserId(),0);
        log.info("-----> 修改订单状态结束");

        log.info("-----> 订单完成！");
    }

}
