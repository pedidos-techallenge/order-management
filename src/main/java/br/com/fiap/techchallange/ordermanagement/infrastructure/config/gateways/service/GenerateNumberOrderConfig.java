package br.com.fiap.techchallange.ordermanagement.infrastructure.config.gateways.service;

import br.com.fiap.techchallange.ordermanagement.adapters.gateways.service.IGenerateNumberOrder;
import br.com.fiap.techchallange.ordermanagement.infrastructure.gateways.GenerateNumberOrderImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GenerateNumberOrderConfig {

    @Bean
    public IGenerateNumberOrder generateNumberOrder() {
        return new GenerateNumberOrderImpl();
    }
} 