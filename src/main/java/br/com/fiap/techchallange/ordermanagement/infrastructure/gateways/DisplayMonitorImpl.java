package br.com.fiap.techchallange.ordermanagement.infrastructure.gateways;

import br.com.fiap.techchallange.ordermanagement.adapters.gateways.service.IDisplayMonitor;
import br.com.fiap.techchallange.ordermanagement.adapters.presenters.viewmodel.OrderViewModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DisplayMonitorImpl implements IDisplayMonitor {
    private static final Logger logger = LoggerFactory.getLogger(DisplayMonitorImpl.class);

    @Override
    public void display(OrderViewModel orderView) {
        logger.info("Display Monitor: {}", orderView);
    }
}