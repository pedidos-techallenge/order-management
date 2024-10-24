package br.com.fiap.techchallange.ordermanagement.infrastructure.repository;

import br.com.fiap.techchallange.ordermanagement.adapters.gateways.repository.IOrderRepository;
import br.com.fiap.techchallange.ordermanagement.core.entity.Order;
import br.com.fiap.techchallange.ordermanagement.core.entity.enums.StatusOrder;
import br.com.fiap.techchallange.ordermanagement.util.OrderHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static br.com.fiap.techchallange.ordermanagement.util.OrderHelper.generateOrder;
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

    @Nested
    public class Criacao{
        @Test
        public void deveCriarPedido(){
            //GIVEN
            Order order = OrderHelper.generateOrder(1, StatusOrder.OPEN);
            when(orderRepository.get(any(String.class))).thenReturn(order);

            //WHEN
            orderRepository.create(order);
            Order orderArmazenada = orderRepository.get(order.getId());

            //THEN
            assertThat(orderArmazenada)
                    .isNotNull()
                    .isEqualTo(order);

            verify(orderRepository, times(1)).create(any(Order.class));
        }
    }

    @Nested
    public class Atualizacao{
        @Test
        public void deveAtualizarPedidoParaRecebido(){
            //GIVEN
            Order order = OrderHelper.generateOrder(1, StatusOrder.OPEN);
            when(orderRepository.get(any(String.class))).thenReturn(order);

            //WHEN
            orderRepository.create(order);
            Order orderArmazenada = orderRepository.get(order.getId());
            order.updateStatus(StatusOrder.RECEIVED);
            orderRepository.update(order);

            //THEN
            assertThat(orderArmazenada)
                    .isNotNull()
                    .isEqualTo(order);

            verify(orderRepository, times(1)).create(any(Order.class));
        }

        @Test
        public void deveAtualizarPedidoParaCancelado(){
            //GIVEN
            Order order = OrderHelper.generateOrder(1, StatusOrder.OPEN);
            when(orderRepository.get(any(String.class))).thenReturn(order);

            //WHEN
            orderRepository.create(order);
            Order orderArmazenada = orderRepository.get(order.getId());
            order.updateStatus(StatusOrder.CANCELED);
            orderRepository.update(order);

            //THEN
            assertThat(orderArmazenada)
                    .isNotNull()
                    .isEqualTo(order);

            verify(orderRepository, times(1)).create(any(Order.class));
        }
    }


    @Nested
    public class Consulta{
        @Test
        public void deveBuscarListaDePedidosEmAtendimento(){
            //GIVEN
            List<Order> pedidos = new ArrayList<>();
            pedidos.add(OrderHelper.generateOrder(1, StatusOrder.RECEIVED));
            pedidos.add(OrderHelper.generateOrder(2, StatusOrder.INPREPARATION));

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

            Order pedido1 = OrderHelper.generateOrder(1, StatusOrder.RECEIVED);
            Order pedido2 = OrderHelper.generateOrder(2, StatusOrder.INPREPARATION);

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
}
