package br.com.fiap.techchallange.ordermanagement.infrastructure.config.gateways;

import br.com.fiap.techchallange.ordermanagement.adapters.gateways.service.IDisplayMonitor;
import br.com.fiap.techchallange.ordermanagement.infrastructure.gateways.DisplayMonitorImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DisplayMonitorConfig {

    @Bean
    public IDisplayMonitor displayMonitor() {
        return new DisplayMonitorImpl();
    }
} 