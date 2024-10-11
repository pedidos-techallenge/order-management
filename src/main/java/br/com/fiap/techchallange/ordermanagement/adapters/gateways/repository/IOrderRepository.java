package br.com.fiap.techchallange.ordermanagement.adapters.gateways.repository;

import br.com.fiap.techchallange.ordermanagement.core.entity.Order;
import br.com.fiap.techchallange.ordermanagement.core.entity.vo.Item;

import java.util.List;

public interface IOrderRepository {

    Order get(String id);
    void create(Order order);
    void update(Order order);
    void addItem(List<Item> items);
    Order getByOrderNumber(int number);
    List<Order> getOrders();
    int getLastNumber();
}
