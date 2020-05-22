package com.km.km_demo;

import com.km.km_demo.middleware.redis.RedisServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
//@EnableDiscoveryClient
//@EnableFeignClients
public class KmDemoApplication {



    public static void main(String[] args) {

        SpringApplication.run(KmDemoApplication.class, args);
        // 系统前置工作：
        // 1.系统设置redis锁
        //new RedisServiceImpl().set("redismatchlock","1");

        //2.
    }


}
