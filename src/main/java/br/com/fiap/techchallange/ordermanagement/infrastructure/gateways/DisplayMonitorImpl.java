package br.com.fiap.techchallange.ordermanagement.infrastructure.gateways;

import br.com.fiap.techchallange.ordermanagement.adapters.gateways.service.IDisplayMonitor;
import br.com.fiap.techchallange.ordermanagement.adapters.presenters.viewmodel.OrderViewModel;
import org.springframework.stereotype.Service;

@Service
public class DisplayMonitorImpl implements IDisplayMonitor {

    @Override
    public void display(OrderViewModel orderView) {
        System.out.println("Display Monitor: " + orderView );
    }
}