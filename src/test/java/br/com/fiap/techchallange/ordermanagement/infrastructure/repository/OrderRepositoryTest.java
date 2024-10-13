package br.com.fiap.techchallange.ordermanagement.infrastructure.repository;

import br.com.fiap.techchallange.ordermanagement.adapters.gateways.repository.IOrderRepository;
import br.com.fiap.techchallange.ordermanagement.core.entity.Order;
import br.com.fiap.techchallange.ordermanagement.core.entity.enums.StatusOrder;
import br.com.fiap.techchallange.ordermanagement.core.entity.vo.Item;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class OrderRepositoryTest {

    @Mock
    private IOrderRepository orderRepository;

    AutoCloseable openMocks;

    @BeforeEach
    public void setup(){
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception{
        openMocks.close();
    }

    private Order gerarPedido(int numberOrder, StatusOrder status){
        //GIVEN
        String idOrder  = "fd0b97c0-3334-4c8e-9d83-ae971b77db99";
        Random random = new Random();


        Item item1 = new Item(idOrder, "123456A", 2, 35);
        Item item2 = new Item(idOrder, "123456B", 1, 15);

        List<Item> items = new ArrayList<>();

        items.add(item1);
        items.add(item2);

        return new Order(idOrder, numberOrder, items, status.getValue());
    }


    @Test
    public void deveCriarPedido(){
        //GIVEN
        Order order = gerarPedido(1, StatusOrder.OPEN);
        when(orderRepository.create(any(Order.class))).thenReturn(order);

        //WHEN
        Order orderArmazenada = orderRepository.create(order);


        //THEN
        assertThat(orderArmazenada)
                .isNotNull()
                .isEqualTo(order);

        verify(orderRepository, times(1)).create(any(Order.class));
    }

    @Test
    public void deveBuscarListaDePedidosEmAtendimento(){
        //GIVEN
        List<Order> pedidos = new ArrayList<>();
        pedidos.add(gerarPedido(1, StatusOrder.RECEIVED));
        pedidos.add(gerarPedido(2, StatusOrder.INPREPARATION));

        when(orderRepository.getOrders()).thenReturn(pedidos);

        // WHEN
        List<Order> pedidosRetornados = orderRepository.getOrders();

        //THEN
        assertThat(pedidosRetornados)
                .asList()
                .hasSize(2);

        verify(orderRepository, times(1)).getOrders();
    }

    @Test
    public void deveBuscarPedidosPeloNumero(){
        //GIVEN
        List<Order> pedidos = new ArrayList<>();

        Order pedido1 = gerarPedido(1, StatusOrder.RECEIVED);
        Order pedido2 = gerarPedido(2, StatusOrder.INPREPARATION);

        when(orderRepository.getByOrderNumber(2)).thenReturn(pedido2);

        // WHEN
        Order pedidosRetornados = orderRepository.getByOrderNumber(2);

        //THEN
        assertThat(pedidosRetornados)
                .isNotNull()
                .isEqualTo(pedido2);

        verify(orderRepository, times(1)).getByOrderNumber(2);
    }
}
