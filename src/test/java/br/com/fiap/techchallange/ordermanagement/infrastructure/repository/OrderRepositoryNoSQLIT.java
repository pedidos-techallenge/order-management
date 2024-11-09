package br.com.fiap.techchallange.ordermanagement.infrastructure.repository;

import br.com.fiap.techchallange.ordermanagement.OrdermanagementApplication;
import br.com.fiap.techchallange.ordermanagement.adapters.gateways.repository.IOrderRepositoryNoSQL;
import br.com.fiap.techchallange.ordermanagement.core.entity.Order;
import br.com.fiap.techchallange.ordermanagement.core.entity.enums.StatusOrder;
import br.com.fiap.techchallange.ordermanagement.core.entity.vo.Item;
import br.com.fiap.techchallange.ordermanagement.infrastructure.config.gateways.repository.RepositoryNoSQLConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.DisabledIf;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static br.com.fiap.techchallange.ordermanagement.util.OrderHelper.generateOrder;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = OrdermanagementApplication.class)
@ActiveProfiles("test")
@DisabledIf(expression = "#{environment['spring.database.sql.enabled'] == 'true'}")
public class OrderRepositoryNoSQLIT {

    @Autowired
    private IOrderRepositoryNoSQL orderRepository;

    @Nested
    public class Setup{
        @Test
        public void devePermitirCriarTabela(){
            var totalDeRegistro = orderRepository.count();
            assertThat(totalDeRegistro).isNotNegative();
        }
    }

    @BeforeEach
    public void cleanUp() {
        orderRepository.clearDB();
    }

    @Nested
    public class Criacao{
        @Test
        public void deveCriarPedido(){
            //GIVEN
            String id = UUID.randomUUID().toString();
            Order order = generateOrder(id, 0, StatusOrder.OPEN);

            //WHEN
            orderRepository.create(order);
            Order orderArmazenada = orderRepository.get(order.getId());

            //THEN
            assertThat(orderArmazenada)
                    .isInstanceOf(Order.class)
                    .isNotNull();

            assertThat(orderArmazenada.getId()).isEqualTo(id);
            assertThat(orderArmazenada.getNumberOrder()).isEqualTo(0);
            assertThat(orderArmazenada.getStatus()).isEqualTo(StatusOrder.OPEN.getValue());
        }
    }

    @Nested
    public class Consulta{
        @Test
        public void deveBuscarListaDePedidosEmAtendimento(){
            //GIVEN
            Order order1 = generateOrder(UUID.randomUUID().toString(),0, StatusOrder.OPEN);
            Order order2 = generateOrder(UUID.randomUUID().toString(),1, StatusOrder.RECEIVED);
            Order order3 = generateOrder(UUID.randomUUID().toString(),2, StatusOrder.INPREPARATION);
            Order order4 = generateOrder(UUID.randomUUID().toString(),3, StatusOrder.CANCELED);

            orderRepository.create(order1);
            orderRepository.create(order2);
            orderRepository.create(order3);
            orderRepository.create(order4);

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
        public void deveBuscarPedidoPeloNumero(){
            //GIVEN
            Order order1 = generateOrder(UUID.randomUUID().toString(),0, StatusOrder.OPEN);
            Order order2 = generateOrder(UUID.randomUUID().toString(),1, StatusOrder.RECEIVED);
            Order order3 = generateOrder(UUID.randomUUID().toString(),2, StatusOrder.INPREPARATION);

            orderRepository.create(order1);
            orderRepository.create(order2);
            orderRepository.create(order3);

            //WHEN
            Order orderRetornado= orderRepository.getByOrderNumber(0);

            //THEN
            assertThat(orderRetornado.getId()).isEqualTo(order1.getId());
        }

        @Test
        public void deveBuscarPedidosPeloId(){
            //GIVEN
            Order order1 = generateOrder(UUID.randomUUID().toString(),0, StatusOrder.OPEN);
            Order order2 = generateOrder(UUID.randomUUID().toString(),1, StatusOrder.RECEIVED);
            Order order3 = generateOrder(UUID.randomUUID().toString(),2, StatusOrder.INPREPARATION);

            orderRepository.create(order1);
            orderRepository.create(order2);
            orderRepository.create(order3);

            //WHEN
            Order orderRetornado= orderRepository.get(order1.getId());

            //THEN
            assertThat(orderRetornado.getId()).isEqualTo(order1.getId());
        }
    }

    @Nested
    public class Alteracao{
        @Test
        public void deveAlterarStatusDoPedido(){
            //GIVEN
            String idOrder = UUID.randomUUID().toString();
            List<Item> items = new ArrayList<>();
            items.add(new Item("123456A", 2, 35));
            items.add(new Item("123456B", 1, 15));

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

}
