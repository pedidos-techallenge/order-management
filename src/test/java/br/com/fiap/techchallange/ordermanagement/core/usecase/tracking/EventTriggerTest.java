package br.com.fiap.techchallange.ordermanagement.core.usecase.tracking;

import br.com.fiap.techchallange.ordermanagement.core.usecase.dto.order.EventOrder;
import br.com.fiap.techchallange.ordermanagement.core.usecase.inputboundary.tracking.IEventListenerOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EventTriggerTest {

    @Mock
    private IEventListenerOrder eventListener;

    private EventTrigger eventTrigger;

    @BeforeEach
    void setUp() {
        eventTrigger = new EventTrigger(eventListener);
    }

    @Test
    void shouldTriggerEventToListener() {
        // Arrange
        EventOrder eventOrder = new EventOrder(1, "paymentApprove", "fd0b97c0-3334-4c8e-9d83-ae971b77db99");

        // Act
        eventTrigger.event(eventOrder);

        // Assert
        verify(eventListener).onEvent(eventOrder);
    }

    @Test
    void shouldTriggerMultipleEventsToListener() {
        // Arrange
        EventOrder event1 = new EventOrder(1, "paymentApprove", "fd0b97c0-3334-4c8e-9d83-ae971b77db99");
        EventOrder event2 = new EventOrder(2, "preparationFood", "fd0b97c0-3334-4c8e-9d83-ae971b77db98");

        // Act
        eventTrigger.event(event1);
        eventTrigger.event(event2);

        // Assert
        verify(eventListener).onEvent(event1);
        verify(eventListener).onEvent(event2);
    }

    @Test
    void shouldTriggerEventWithNullValues() {
        // Arrange
        String orderId = "fd0b97c0-3334-4c8e-9d83-ae971b77db99";
        EventOrder eventOrder = new EventOrder(1, "paymentApprove", orderId);

        // Act
        eventTrigger.event(eventOrder);

        // Assert
        verify(eventListener).onEvent(eventOrder);
    }
} 