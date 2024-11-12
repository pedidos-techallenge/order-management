package br.com.fiap.techchallange.ordermanagement.core.usecase.tracking;

import br.com.fiap.techchallange.ordermanagement.adapters.gateways.repository.IOrderRepository;
import br.com.fiap.techchallange.ordermanagement.core.entity.Order;
import br.com.fiap.techchallange.ordermanagement.core.usecase.dto.order.OutputDataOrderDTO;
import br.com.fiap.techchallange.ordermanagement.core.usecase.inputboundary.tracking.IOrderListingUseCase;

import java.util.ArrayList;
import java.util.List;

public class OrderListing implements IOrderListingUseCase {

    private final IOrderRepository orderRepository;

    public OrderListing(IOrderRepository orderRepository){
        this.orderRepository =  orderRepository;
    }

    @Override
    public List<OutputDataOrderDTO> invoke() {
        List<Order> orders = orderRepository.getOrders();
        List<OutputDataOrderDTO> ordersDTO = new ArrayList<>();
        for(Order order: orders){
            ordersDTO.add(new OutputDataOrderDTO(order.getId(), order.getNumberOrder(), order.getStatus()));
        }

        return ordersDTO;
    }

    @Override
    public OutputDataOrderDTO invoke(String idOrder) {
        if (idOrder == null) {
            throw new IllegalArgumentException("Order ID cannot be null");
        }
        
        Order order = this.orderRepository.get(idOrder);
        if (order == null) {
            throw new NullPointerException("Order not found");
        }
        
        return new OutputDataOrderDTO(order.getId(), order.getNumberOrder(), order.getStatus());
    }
}
