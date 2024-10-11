package br.com.fiap.techchallange.ordermanagement.core.usecase.outputboundary.gateways.bd;

import br.com.fiap.techchallange.ordermanagement.core.entity.Order;

public interface IOrderGatewayBD {

    public void registerOrder(Order order);
}
