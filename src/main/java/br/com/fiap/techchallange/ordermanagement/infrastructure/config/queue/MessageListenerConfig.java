package br.com.fiap.techchallange.ordermanagement.infrastructure.config.queue;

import br.com.fiap.techchallange.ordermanagement.adapters.gateways.queue.IMessageListener;
import br.com.fiap.techchallange.ordermanagement.infrastructure.queue.SqsMessageListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageListenerConfig {

    @Bean
    public IMessageListener getMessageListener(){
        return new SqsMessageListener();
    }
}
