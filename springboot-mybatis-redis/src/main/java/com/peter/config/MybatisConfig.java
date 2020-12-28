package com.peter.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan({"com.peter.mapper"})
public class MybatisConfig {
}
