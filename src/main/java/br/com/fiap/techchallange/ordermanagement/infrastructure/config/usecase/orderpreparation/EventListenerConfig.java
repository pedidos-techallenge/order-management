package br.com.fiap.techchallange.ordermanagement.infrastructure.config.usecase.orderpreparation;

import br.com.fiap.techchallange.ordermanagement.core.usecase.inputboundary.tracking.IEventListenerOrder;
import br.com.fiap.techchallange.ordermanagement.core.usecase.inputboundary.tracking.IEventTrigger;
import br.com.fiap.techchallange.ordermanagement.core.usecase.tracking.EventTrigger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventListenerConfig {

    @Bean
    public IEventTrigger getEventTrigger(IEventListenerOrder listenerOrder){
        return new EventTrigger(listenerOrder);
    }
}
