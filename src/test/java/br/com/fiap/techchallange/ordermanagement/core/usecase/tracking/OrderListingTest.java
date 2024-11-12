package br.com.fiap.techchallange.ordermanagement.core.usecase.tracking;

import br.com.fiap.techchallange.ordermanagement.adapters.gateways.repository.IOrderRepository;
import br.com.fiap.techchallange.ordermanagement.core.entity.Order;
import br.com.fiap.techchallange.ordermanagement.core.entity.enums.StatusOrder;
import br.com.fiap.techchallange.ordermanagement.core.usecase.dto.order.OutputDataOrderDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderListingTest {

//    @Mock
//    private IOrderRepository orderRepository;
//
//    private OrderListing orderListing;
//
//    @BeforeEach
//    void setUp() {
//        orderListing = new OrderListing(orderRepository);
//    }
//
//    @Test
//    void shouldReturnOrderDTOWhenOrderExists() {
//        // Arrange
//        String orderId = "123e4567-e89b-12d3-a456-426614174000";
//        Order mockOrder = mock(Order.class);
//        when(mockOrder.getId()).thenReturn(orderId);
//        when(mockOrder.getNumberOrder()).thenReturn(1);
//        when(mockOrder.getStatus()).thenReturn(StatusOrder.RECEIVED.getValue());
//
//        when(orderRepository.get(orderId)).thenReturn(mockOrder);
//
//        // Act
//        OutputDataOrderDTO result = orderListing.invoke(orderId);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(orderId, result.id());
//        assertEquals(1, result.number_order());
//        assertEquals(StatusOrder.RECEIVED.getValue(), result.status());
//        verify(orderRepository).get(orderId);
//    }
//
//    @Test
//    void shouldThrowExceptionWhenOrderNotFound() {
//        // Arrange
//        String orderId = "non-existent-id";
//        when(orderRepository.get(orderId)).thenReturn(null);
//
//        // Act & Assert
//        assertThrows(NullPointerException.class, () -> {
//            orderListing.invoke(orderId);
//        });
//        verify(orderRepository).get(orderId);
//    }
//
//    @Test
//    void shouldReturnOrderDTOWithDifferentStatus() {
//        // Arrange
//        String orderId = "123e4567-e89b-12d3-a456-426614174000";
//        Order mockOrder = mock(Order.class);
//        when(mockOrder.getId()).thenReturn(orderId);
//        when(mockOrder.getNumberOrder()).thenReturn(2);
//        when(mockOrder.getStatus()).thenReturn(StatusOrder.INPREPARATION.getValue());
//
//        when(orderRepository.get(orderId)).thenReturn(mockOrder);
//
//        // Act
//        OutputDataOrderDTO result = orderListing.invoke(orderId);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(orderId, result.id());
//        assertEquals(2, result.number_order());
//        assertEquals(StatusOrder.INPREPARATION.getValue(), result.status());
//        verify(orderRepository).get(orderId);
//    }
//
//    @Test
//    void shouldHandleOrderWithNullId() {
//        // Arrange
//        String orderId = null;
//
//        // Act & Assert
//        assertThrows(IllegalArgumentException.class, () -> {
//            orderListing.invoke(orderId);
//        });
//        verify(orderRepository, never()).get(any());
//    }
} 