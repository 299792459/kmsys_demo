package com.km.km_demo.middleware.mq;

import com.km.km_demo.util.myResult;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author 陈景
 * @QQ:895373488
 * @create 2020/4/9 0009
 * @since 1.0.0
 */
public class rabbitMqReceiver {

    @RabbitListener(queues="kmqueue")    //监听器监听指定的Queue

    public void processC(myResult rs) {
        System.out.println("\nReceive:" + rs.getMessage());

        //收到新发来的匹配请求，则将其加入匹配队列

    }
}
