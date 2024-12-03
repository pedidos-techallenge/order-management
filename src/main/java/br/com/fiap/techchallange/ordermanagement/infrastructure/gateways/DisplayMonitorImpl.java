package br.com.fiap.techchallange.ordermanagement.infrastructure.gateways;

import br.com.fiap.techchallange.ordermanagement.adapters.gateways.service.IDisplayMonitor;
import br.com.fiap.techchallange.ordermanagement.adapters.presenters.viewmodel.OrderViewModel;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class DisplayMonitorImpl implements IDisplayMonitor {
    private final Logger logger;

    public DisplayMonitorImpl(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void display(OrderViewModel orderView) {
        logger.info("Display Monitor: {}", orderView);
    }
}