package br.com.fiap.techchallange.ordermanagement.infrastructure.config.gateways.services;

import br.com.fiap.techchallange.ordermanagement.adapters.controllers.tracking.IGetLatestOrderNumberController;
import br.com.fiap.techchallange.ordermanagement.adapters.gateways.service.IDisplayMonitor;
import br.com.fiap.techchallange.ordermanagement.adapters.gateways.service.IGenerateNumberOrder;
import br.com.fiap.techchallange.ordermanagement.infrastructure.service.DisplayMonitorConsole;
import br.com.fiap.techchallange.ordermanagement.infrastructure.service.GenerateNumberOrder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    @Bean
    public IDisplayMonitor getDisplayMonitor(){
        return new DisplayMonitorConsole();
    }

    @Bean
    public IGenerateNumberOrder getGenerateNumberOrder(IGetLatestOrderNumberController getLatestOrderNumberController){
        return new GenerateNumberOrder(getLatestOrderNumberController);
    }
}
