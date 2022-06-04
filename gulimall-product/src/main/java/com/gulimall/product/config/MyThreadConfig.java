package com.gulimall.product.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * @author zy
 * @create 2022-06-04-22:24
 */
@Configuration
@PropertySource("classpath:ThreadApplication.properties")
public class MyThreadConfig {
    @Value("${thread.corePoolSize}")
    private Integer corePoolSizeNum;
    @Value("${thread.maximumPoolSize}")
    private Integer maximumPoolSizeNum;
    @Value("${thread.keepAliveTime}")
    private Integer keepAliveTimeUnm;
    @Bean
    public ThreadPoolExecutor threadPoolExecutor() {
        return new ThreadPoolExecutor(
                corePoolSizeNum,
                maximumPoolSizeNum,
                keepAliveTimeUnm,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(100000),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
    }
}
