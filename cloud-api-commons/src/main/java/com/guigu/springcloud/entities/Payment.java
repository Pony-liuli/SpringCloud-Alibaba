package com.guigu.springcloud.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data // get set
@AllArgsConstructor // 全参构造函数
@NoArgsConstructor  // 空参构造函数
public class Payment implements Serializable {

    private Long id;
    private String serial;
}

