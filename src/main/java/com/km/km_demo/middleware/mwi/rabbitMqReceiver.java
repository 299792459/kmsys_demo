package com.km.km_demo.middleware.mwi;


import org.springframework.stereotype.Component;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 陈景
 * @QQ:895373488
 * @create 2020/4/9 0009
 * @since 1.0.0
 */
@Component
public interface rabbitMqReceiver {

    public void processC(String s);
}
