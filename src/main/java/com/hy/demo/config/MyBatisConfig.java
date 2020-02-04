package com.hy.demo.config;

import org.springframework.context.annotation.Configuration;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * MyBatis配置类
 * Created by macro on 2019/4/8.
 */
@Configuration
@MapperScan({"com.hy.demo.mapper","com.hy.demo.dao"})
public class MyBatisConfig {
}
