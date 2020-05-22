package com.km.km_demo.middleware.mq.Impl;



import com.km.km_demo.middleware.mwi.rabbitMqSender;
import com.km.km_demo.middleware.redis.RedisServiceImpl;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
public class RabbitMqSenderImpl implements rabbitMqSender {

    @Autowired
    RedisServiceImpl rsi;

    @Autowired
    private AmqpTemplate template;

    public void send(String s,String message) {
        template.convertAndSend("kmqueue",message);
        rsi.set("mqdbsend"+s,message);

    }
}
