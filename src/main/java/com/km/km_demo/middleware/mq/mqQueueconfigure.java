package com.km.km_demo.middleware.mq;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;

@Configuration
public class mqQueueconfigure {
        @Bean
        public Queue queue() {
            return new Queue("kmqueue");
    }
}