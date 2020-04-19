package com.km.km_demo.util;

import org.springframework.stereotype.Component;

/**
 * 〈一句话功能简述〉<br>
 * 〈随机数工具类〉
 *
 * @author 陈景
 * @QQ:895373488
 * @create 2020/4/7 0007
 * @since 1.0.0
 */
@Component
public class myrandom {

    public String generatecode()
    {
        int max=10000,min=1000;
        int ran = (int) (Math.random()*1000);
        System.out.println(ran);
        return ""+ran;
    }
}
