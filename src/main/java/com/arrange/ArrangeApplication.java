package com.arrange;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@ServletComponentScan
@EnableScheduling
@EnableTransactionManagement
@EnableCaching//通过@EnableCaching注解自动化配置合适的缓存管理器（CacheManager）
@MapperScan("com.arrange.dao.mapper")
public class ArrangeApplication {
    @PostConstruct
    void started() {//设置时区
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
    }

    public static void main(String[] args) {
        SpringApplication.run(ArrangeApplication.class, args);
    }

}
