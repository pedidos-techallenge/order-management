package br.com.fiap.techchallange.ordermanagement.infrastructure.queue;

import br.com.fiap.techchallange.ordermanagement.core.entity.enums.StatusPayment;
import br.com.fiap.techchallange.ordermanagement.core.entity.vo.EventPayment;
import br.com.fiap.techchallange.ordermanagement.core.usecase.inputboundary.processingpayment.IPaymentProcessingUseCase;
import com.fasterxml.jackson.core.JsonParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class SqsMessageListenerTest {

    @InjectMocks
    private SqsMessageListener sqsMessageListener;

    @Mock
    private IPaymentProcessingUseCase paymentProcessingUseCase;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);       
    }

    @Test
    public void testConsumer() throws IOException {
        // Arrange
        String message = "{\"idOrder\":\"12345\", \"statusPayment\":\"PAID\"}";       

        // Act
        sqsMessageListener.consumer(message);

        // Assert
        ArgumentCaptor<EventPayment> eventPaymentCaptor = ArgumentCaptor.forClass(EventPayment.class);
        verify(paymentProcessingUseCase).invoke(eventPaymentCaptor.capture());

        EventPayment capturedEventPayment = eventPaymentCaptor.getValue();
        assertEquals("12345", capturedEventPayment.idOrder());
        assertEquals(StatusPayment.PAID, capturedEventPayment.statusPayment());
        assertNotNull(capturedEventPayment);
    }

    @Test
    public void testConsumerWithInvalidJson() {
        // Arrange
        String invalidMessage = "invalid json";

        // Act & Assert
        JsonParseException exception = assertThrows(JsonParseException.class, () -> {
            sqsMessageListener.consumer(invalidMessage);
        });

        // Verifica se a exceção lançada é do tipo JsonParseException
        assertEquals(JsonParseException.class, exception.getClass());
    }
}