package br.com.fiap.techchallange.ordermanagement.adapters.gateways.repository;

import br.com.fiap.techchallange.ordermanagement.core.entity.Order;

import java.util.List;

public interface IOrderRepository {

    Order get(String id);
    void create(Order order);
    void update(Order order);
    Order getByOrderNumber(int number);
    List<Order> getOrders();
    int getLastNumber();
    int count();
    void clearDB();
}
