package br.com.fiap.techchallange.ordermanagement.infrastructure.repository;

import br.com.fiap.techchallange.ordermanagement.adapters.gateways.repository.IOrderRepository;
import br.com.fiap.techchallange.ordermanagement.core.entity.Order;
import br.com.fiap.techchallange.ordermanagement.core.entity.enums.StatusOrder;
import br.com.fiap.techchallange.ordermanagement.core.entity.vo.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
@ActiveProfiles("test")
@ComponentScan
public class
OrderRepositoryIT {

    @Autowired
    private IOrderRepository orderRepository;
    private Order gerarPedido(String id, int numberOrder, StatusOrder status){
        //GIVEN
        String idOrder  = id;
        Random random = new Random();


        Item item1 = new Item(idOrder, "123456A", 2, 35);
        Item item2 = new Item(idOrder, "123456B", 1, 15);

        List<Item> items = new ArrayList<>();

        items.add(item1);
        items.add(item2);

        return new Order(idOrder, numberOrder, items, status.getValue());
    }

    @Test
    public void devePermitirCriarTabela(){
        var totalDeRegistro = orderRepository.count();
        assertThat(totalDeRegistro).isNotNegative();
    }

    @Test
    public void deveCriarPedido(){
        //GIVEN
        String id = UUID.randomUUID().toString();
        Order order = gerarPedido(id,0, StatusOrder.OPEN);

        //WHEN
        Order orderArmazenada = orderRepository.create(order);

        //THEN
        assertThat(orderArmazenada)
                .isInstanceOf(Order.class)
                .isNotNull();

        assertThat(orderArmazenada.getId()).isEqualTo(id);
        assertThat(orderArmazenada.getNumberOrder()).isEqualTo(0);
        assertThat(orderArmazenada.getStatus()).isEqualTo(StatusOrder.OPEN.getValue());
    }

    @Test
    public void deveBuscarListaDePedidosEmAtendimento(){
        //GIVEN
        Order order1 = gerarPedido(UUID.randomUUID().toString(),0, StatusOrder.OPEN);
        Order order2 = gerarPedido(UUID.randomUUID().toString(),1, StatusOrder.RECEIVED);
        Order order3 = gerarPedido(UUID.randomUUID().toString(),2, StatusOrder.INPREPARATION);

        orderRepository.create(order1);
        orderRepository.create(order2);
        orderRepository.create(order3);

        //WHEN
        List<Order> orders = orderRepository.getOrders();
        Order primeiroElementoDaLista = orders.get(0);

        //THEN
        assertThat(orders)
                .asList()
                .hasSize(2);

        assertThat(primeiroElementoDaLista.getId()).isEqualTo(order3.getId());
    }

    @Test
    public void deveBuscarPedidosPeloNumero(){
        //GIVEN
        Order order1 = gerarPedido(UUID.randomUUID().toString(),0, StatusOrder.OPEN);
        Order order2 = gerarPedido(UUID.randomUUID().toString(),1, StatusOrder.RECEIVED);
        Order order3 = gerarPedido(UUID.randomUUID().toString(),2, StatusOrder.INPREPARATION);

        orderRepository.create(order1);
        orderRepository.create(order2);
        orderRepository.create(order3);

        //WHEN
        Order orderRetornado= orderRepository.getByOrderNumber(0);

        //THEN
        assertThat(orderRetornado.getId()).isEqualTo(order1.getId());
    }

    @Test
    public void deveAlterarStatusDoPedido(){
        //GIVEN
        String idOrder = UUID.randomUUID().toString();
        List<Item> items = new ArrayList<>();
        items.add(new Item(idOrder, "123456A", 2, 35));
        items.add(new Item(idOrder, "123456B", 1, 15));

        Order order = new Order(idOrder,
                                1,
                                items,
                                StatusOrder.RECEIVED.getValue()
        );

        orderRepository.create(order);

        //WHEN
        order.updateStatus(StatusOrder.INPREPARATION);
        order.setNumberOrder(1);
        orderRepository.update(order);

        Order orderRetornado = orderRepository.getByOrderNumber(1);

        //THEN
        assertThat(orderRetornado.getStatus()).isEqualTo(StatusOrder.INPREPARATION.getValue());
        assertThat(orderRetornado.getId()).isEqualTo(order.getId());
    }
}
