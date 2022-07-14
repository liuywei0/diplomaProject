package com.liuywei.housekeeping.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.liuywei.housekeeping.dao")
public class MybatisConfig {
}
