package br.com.fiap.techchallange.ordermanagement.adapters.gateways.service;

import br.com.fiap.techchallange.ordermanagement.adapters.presenters.viewmodel.OrderViewModel;

public interface IDisplayMonitor {
    public void display(OrderViewModel orderView);
}
