package com.km.km_demo.middleware.mq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class mqQueueconfigure {
        @Bean
        public Queue queue() {
            return new Queue("kmqueue");
    }
}