package br.com.fiap.techchallange.ordermanagement.infrastructure.service;

import br.com.fiap.techchallange.ordermanagement.adapters.gateways.service.IDisplayMonitor;
import br.com.fiap.techchallange.ordermanagement.adapters.presenters.viewmodel.OrderViewModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DisplayMonitorWeb implements IDisplayMonitor {

    private final RestTemplate restTemplate;
    private final String monitorUrl;

    
    public DisplayMonitorWeb() {
        this(new RestTemplate(), "http://localhost:8081/notify");
    }

    @Autowired
    public DisplayMonitorWeb(RestTemplate restTemplate, String monitorUrl) {
        this.restTemplate = restTemplate;
        this.monitorUrl = monitorUrl;
    }

    @Override
    public void display(OrderViewModel orderView) {
        OrderNotification order = new OrderNotification(
            orderView.number_order(), 
            orderView.status()
        );
        restTemplate.postForEntity(monitorUrl, order, Void.class);
    }

    public record OrderNotification(int orderId, String status){}
}
