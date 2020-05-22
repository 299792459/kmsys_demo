package com.km.km_demo.middleware.mwi;


import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 陈景
 * @QQ:895373488
 * @create 2020/4/10 0010
 * @since 1.0.0
 */
@Component
@EnableAutoConfiguration
//@FeignClient(value = "kmCloudMiddleWare")
@RestController
public interface rabbitMqSender {
    //消息队列
    @RequestMapping("/sendsm")
    public void send(String s, String message);

}
