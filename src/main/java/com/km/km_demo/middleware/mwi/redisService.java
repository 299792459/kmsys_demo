package com.km.km_demo.middleware.mwi;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Set;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 陈景
 * @QQ:895373488
 * @create 2020/4/24 0024
 * @since 1.0.0
 */
//@FeignClient(value = "bookCI",fallback = redisHystrix.class)
@RequestMapping("/redis")
public interface redisService {

    @RequestMapping("/set12")
    boolean set(String var1, Object var2);
    @RequestMapping("/set123")
    boolean set(String var1, Object var2, Long var3);
    @RequestMapping("/removen")
    void remove(String... var1);
    @RequestMapping("/removePattern")
    void removePattern(String var1);
    @RequestMapping("/remove1")
    void remove(String var1);
    @RequestMapping("/exists")
    boolean exists(String var1);
    @RequestMapping("/get")
    Object get(String var1);

    void hmSet(String var1, Object var2, Object var3);

    Object hmGet(String var1, Object var2);

    void lPush(String var1, Object var2);

    List<Object> lRange(String var1, long var2, long var4);
    @RequestMapping("/add")
    void add(String var1, Object var2);

    Set<Object> setMembers(String var1);

    void zAdd(String var1, Object var2, double var3);

    Set<Object> rangeByScore(String var1, double var2, double var4);
}
