package br.com.fiap.techchallange.ordermanagement.core.usecase.tracking;

import br.com.fiap.techchallange.ordermanagement.adapters.gateways.repository.IOrderRepository;
import br.com.fiap.techchallange.ordermanagement.core.entity.Order;
import br.com.fiap.techchallange.ordermanagement.core.entity.enums.StatusOrder;
import br.com.fiap.techchallange.ordermanagement.core.usecase.dto.order.EventOrder;
import br.com.fiap.techchallange.ordermanagement.core.usecase.dto.order.OutputDataOrderDTO;
import br.com.fiap.techchallange.ordermanagement.core.usecase.outputboundary.presenters.tracking.IDisplayInformationOrderPresenter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderUpdateStatusTest {

    @Mock
    private IOrderRepository orderRepository;

    @Mock
    private IDisplayInformationOrderPresenter displayInformationOrderPresenter;

    private OrderUpdateStatus orderUpdateStatus;

    @BeforeEach
    void setUp() {
        orderUpdateStatus = new OrderUpdateStatus(orderRepository, displayInformationOrderPresenter);
    }

    @Test
    void shouldUpdateOrderStatusWhenReceivingPaymentApproveEvent() {
        // Arrange
        String orderId = "fd0b97c0-3334-4c8e-9d83-ae971b77db99";
        EventOrder event = new EventOrder(1, "paymentApprove", orderId);
        Order order = mock(Order.class);
        when(orderRepository.get(orderId)).thenReturn(order);

        // Act
        orderUpdateStatus.onEvent(event);

        // Assert
        verify(order).updateStatus(StatusOrder.RECEIVED);
        verify(orderRepository).update(order);
        verify(displayInformationOrderPresenter).display(any(OutputDataOrderDTO.class));
    }

    @Test
    void shouldUpdateOrderStatusWhenReceivingPaymentDeniedEvent() {
        // Arrange
        String orderId = "fd0b97c0-3334-4c8e-9d83-ae971b77db99";
        EventOrder event = new EventOrder(1, "paymentDenied", orderId);
        Order order = mock(Order.class);
        when(orderRepository.get(orderId)).thenReturn(order);

        // Act
        orderUpdateStatus.onEvent(event);

        // Assert
        verify(order).updateStatus(StatusOrder.CANCELED);
        verify(orderRepository).update(order);
        verify(displayInformationOrderPresenter).display(any(OutputDataOrderDTO.class));
    }

    @Test
    void shouldUpdateOrderStatusWhenReceivingPreparationFoodEvent() {
        // Arrange
        String orderId = "fd0b97c0-3334-4c8e-9d83-ae971b77db99";
        EventOrder event = new EventOrder(1, "preparationFood", orderId);
        Order order = mock(Order.class);
        when(orderRepository.get(orderId)).thenReturn(order);

        // Act
        orderUpdateStatus.onEvent(event);

        // Assert
        verify(order).updateStatus(StatusOrder.INPREPARATION);
        verify(orderRepository).update(order);
        verify(displayInformationOrderPresenter).display(any(OutputDataOrderDTO.class));
    }

    @Test
    void shouldUpdateOrderStatusWhenReceivingFoodDoneEvent() {
        // Arrange
        String orderId = "fd0b97c0-3334-4c8e-9d83-ae971b77db99";
        EventOrder event = new EventOrder(1, "foodDone", orderId);
        Order order = mock(Order.class);
        when(orderRepository.get(orderId)).thenReturn(order);

        // Act
        orderUpdateStatus.onEvent(event);

        // Assert
        verify(order).updateStatus(StatusOrder.FOODDONE);
        verify(orderRepository).update(order);
        verify(displayInformationOrderPresenter).display(any(OutputDataOrderDTO.class));
    }

    @Test
    void shouldUpdateOrderStatusWhenReceivingDeliveryFoodEvent() {
        // Arrange
        String orderId = "fd0b97c0-3334-4c8e-9d83-ae971b77db99";
        EventOrder event = new EventOrder(1, "deliveryFood", orderId);
        Order order = mock(Order.class);
        when(orderRepository.get(orderId)).thenReturn(order);

        // Act
        orderUpdateStatus.onEvent(event);

        // Assert
        verify(order).updateStatus(StatusOrder.FINISHED);
        verify(orderRepository).update(order);
        verify(displayInformationOrderPresenter).display(any(OutputDataOrderDTO.class));
    }

    @Test
    void shouldDoNothingWhenOrderNotFound() {
        // Arrange
        String orderId = "fd0b97c0-3334-4c8e-9d83-ae971b77db99";
        EventOrder event = new EventOrder(1, "paymentApprove", orderId);
        when(orderRepository.get(orderId)).thenReturn(null);

        // Act
        orderUpdateStatus.onEvent(event);

        // Assert
        verify(orderRepository, never()).update(any());
        verify(displayInformationOrderPresenter, never()).display(any());
    }

    @Test
    void shouldThrowExceptionWhenReceivingUnmappedEvent() {
        // Arrange
        String orderId = "fd0b97c0-3334-4c8e-9d83-ae971b77db99";
        EventOrder event = new EventOrder(1, "invalidEvent", orderId);

        // Act & Assert
        org.junit.jupiter.api.Assertions.assertThrows(
            RuntimeException.class,
            () -> orderUpdateStatus.onEvent(event),
            "Event don't mapped"
        );
    }
} 