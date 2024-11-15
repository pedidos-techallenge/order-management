package br.com.fiap.techchallange.ordermanagement.infrastructure.repository;

import br.com.fiap.techchallange.ordermanagement.core.entity.Order;
import br.com.fiap.techchallange.ordermanagement.core.entity.enums.StatusOrder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DocumentDBOrderRepositoryTest {

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private DocumentDBOrderRepository repository;

    @BeforeEach
    void setUp() {
        repository.clearDB();
    }

    @Test
    void shouldCreateAndGetOrder() {
        // Arrange
        Order order = new Order("123");
        order.setNumberOrder(1);
        order.updateStatus(StatusOrder.RECEIVED);

        // Mock para findOne com Query
        when(mongoTemplate.findOne(any(Query.class), eq(Order.class))).thenReturn(order);
        when(mongoTemplate.save(any(Order.class))).thenReturn(order);

        // Act
        repository.create(order);
        Order savedOrder = repository.get("123");

        // Assert
        assertNotNull(savedOrder);
        assertEquals(1, savedOrder.getNumberOrder());
        
        // Verify
        verify(mongoTemplate).save(order);
        verify(mongoTemplate).findOne(any(Query.class), eq(Order.class));
    }

    @Test
    void shouldUpdateOrder() {
        // Arrange
        Order order = new Order("123");
        order.setNumberOrder(1);
        order.updateStatus(StatusOrder.RECEIVED);

        when(mongoTemplate.findOne(any(Query.class), eq(Order.class))).thenReturn(order);
        when(mongoTemplate.save(any(Order.class))).thenReturn(order);

        // Act
        repository.create(order);
        order.updateStatus(StatusOrder.INPREPARATION);
        repository.update(order);
        Order updatedOrder = repository.get(order.getId());

        // Assert
        assertEquals(StatusOrder.INPREPARATION.getValue(), updatedOrder.getStatus());
        
        // Verify
        verify(mongoTemplate, times(2)).save(order);
        verify(mongoTemplate).findOne(any(Query.class), eq(Order.class));
    }

    @Test
    void shouldGetByOrderNumber() {
        // Arrange
        Order order = new Order();
        order.setNumberOrder(1);
        
        // Configure o mock para retornar o pedido quando findOne for chamado
        when(mongoTemplate.findOne(any(), eq(Order.class))).thenReturn(order);

        // Act
        Order foundOrder = repository.getByOrderNumber(1);

        // Assert
        assertNotNull(foundOrder);
        assertEquals(1, foundOrder.getNumberOrder());
        verify(mongoTemplate).findOne(any(), eq(Order.class));
    }

    @Test
    void shouldGetOrdersSortedByStatusAndNumber() {
        // Arrange
        List<Order> orders = new ArrayList<>();
        
        // Criando pedidos em diferentes status seguindo a sequência correta
        Order order1 = new Order("1");
        order1.setNumberOrder(1);
        order1.updateStatus(StatusOrder.RECEIVED);
        orders.add(order1);

        Order order2 = new Order("2");
        order2.setNumberOrder(2);
        order2.updateStatus(StatusOrder.RECEIVED);
        order2.updateStatus(StatusOrder.INPREPARATION);
        orders.add(order2);

        Order order3 = new Order("3");
        order3.setNumberOrder(3);
        order3.updateStatus(StatusOrder.RECEIVED);
        order3.updateStatus(StatusOrder.INPREPARATION);
        order3.updateStatus(StatusOrder.FOODDONE);
        orders.add(order3);

        Order order4 = new Order("4");
        order4.setNumberOrder(4);
        order4.updateStatus(StatusOrder.RECEIVED);
        order4.updateStatus(StatusOrder.INPREPARATION);
        order4.updateStatus(StatusOrder.FOODDONE);
        order4.updateStatus(StatusOrder.FINISHED);
        orders.add(order4);

        when(mongoTemplate.findAll(Order.class)).thenReturn(orders);

        // Act
        List<Order> result = repository.getOrders();

        // Assert
        assertEquals(3, result.size()); // apenas RECEIVED, INPREPARATION e FOODDONE
        assertEquals(StatusOrder.FOODDONE.getValue(), result.get(0).getStatus());
        assertEquals(StatusOrder.INPREPARATION.getValue(), result.get(1).getStatus());
        assertEquals(StatusOrder.RECEIVED.getValue(), result.get(2).getStatus());
    }

    @Test
    void shouldGetLastNumber() {
        // Arrange
        Order order = new Order("123");
        order.setNumberOrder(3);
        
        // Mock para findOne com Query ordenada
        when(mongoTemplate.findOne(any(Query.class), eq(Order.class))).thenReturn(order);

        // Act
        int lastNumber = repository.getLastNumber();

        // Assert
        assertEquals(3, lastNumber);
        verify(mongoTemplate).findOne(any(Query.class), eq(Order.class));
    }

    @Test
    void shouldReturnZeroWhenNoOrders() {
        // Mock para retornar null quando não houver pedidos
        when(mongoTemplate.findOne(any(Query.class), eq(Order.class))).thenReturn(null);

        // Act
        int lastNumber = repository.getLastNumber();

        // Assert
        assertEquals(0, lastNumber);
        verify(mongoTemplate).findOne(any(Query.class), eq(Order.class));
    }

    @Test
    void shouldCountOrders() {
        // Arrange
        createOrderWithStatus(1, StatusOrder.valueOf("RECEIVED"));
        createOrderWithStatus(2, StatusOrder.valueOf("RECEIVED"));
        
        // Configure o mock para retornar 2 quando count() for chamado
        when(mongoTemplate.count(any(), eq(Order.class))).thenReturn(2L);

        // Act
        int count = repository.count();

        // Assert
        assertEquals(2, count);
        verify(mongoTemplate).count(any(), eq(Order.class));
    }

    private Order createOrderWithStatus(int number, StatusOrder status) {
        Order order = new Order(String.valueOf(number));
        order.setNumberOrder(number);
        order.updateStatus(status);
        return order;
    }
} 