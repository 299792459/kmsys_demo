package com.km.km_demo.middleware.mq.Impl;



import com.km.km_demo.matchsys.matchsystem;
import com.km.km_demo.middleware.mwi.rabbitMqReceiver;
import com.km.km_demo.middleware.redis.RedisServiceImpl;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
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
public class rabbitMqReceiverImpl implements rabbitMqReceiver {

    @Autowired
    RedisServiceImpl rsi;

    @Autowired
    matchsystem ms;

    @RabbitListener(queues="kmqueue")    //监听器监听指定的Queue
    public void processC(String s) {
        System.out.println("\nReceive:" + s);
        rsi.set("mqdbrecivekmqueue",s);
        if(s.equals("match")){
            ms.ms("matchQueue");
        }
        //收到新发来的匹配请求，则将其加入匹配队列
    }
}
