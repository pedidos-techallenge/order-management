package br.com.fiap.techchallange.ordermanagement.core.usecase.orderpayment;

import br.com.fiap.techchallange.ordermanagement.adapters.gateways.repository.IOrderRepository;
import br.com.fiap.techchallange.ordermanagement.adapters.gateways.service.IGenerateNumberOrder;
import br.com.fiap.techchallange.ordermanagement.core.entity.Order;
import br.com.fiap.techchallange.ordermanagement.core.entity.enums.StatusPayment;
import br.com.fiap.techchallange.ordermanagement.core.entity.vo.EventPayment;
import br.com.fiap.techchallange.ordermanagement.core.usecase.inputboundary.tracking.IEventTrigger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentProcessingUseCaseTest {
    
    @Mock
    private IOrderRepository repositoryOrder;
    
    @Mock
    private IEventTrigger trigger;
    
    @Mock
    private IGenerateNumberOrder generateNumberOrder;
    
    @InjectMocks
    private PaymentProcessingUseCase paymentProcessingUseCase;
    
    @Test
    @DisplayName("Deve processar pagamento aprovado com sucesso")
    void shouldProcessApprovedPaymentSuccessfully() throws IOException {
        // Arrange
        String orderId = "123";
        int numberOrder = 1;
        EventPayment eventPayment = new EventPayment(
            orderId,
            StatusPayment.PAID,
                LocalDateTime.now()
        );
        
        Order order = new Order(orderId);

        
        when(repositoryOrder.get(orderId)).thenReturn(order);
        when(generateNumberOrder.generate()).thenReturn(numberOrder);
        
        // Act
        paymentProcessingUseCase.invoke(eventPayment);
        
        // Assert
        verify(repositoryOrder).get(orderId);
        verify(generateNumberOrder).generate();
        verify(repositoryOrder).update(order);
        verify(trigger).event(argThat(eventOrder -> 
            eventOrder.number_order() == numberOrder &&
                    Objects.equals(eventOrder.idOrder(), orderId)
        ));
    }
    
    @Test
    @DisplayName("Deve processar pagamento negado com sucesso")
    void shouldProcessDeniedPaymentSuccessfully() throws IOException {
        // Arrange
        String orderId = "123";
        int numberOrder = 1;
        EventPayment eventPayment = new EventPayment(
            orderId,
            StatusPayment.DENIED,
            LocalDateTime.now()
        );
        
        Order order = new Order(orderId);

        when(repositoryOrder.get(orderId)).thenReturn(order);
        when(generateNumberOrder.generate()).thenReturn(numberOrder);
        
        // Act
        paymentProcessingUseCase.invoke(eventPayment);
        
        // Assert
        verify(repositoryOrder).get(orderId);
        verify(generateNumberOrder).generate();
        verify(repositoryOrder).update(order);
        verify(trigger).event(argThat(eventOrder -> 
            eventOrder.number_order() == numberOrder &&
            eventOrder.idOrder().equals(orderId)
        ));
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando status do pagamento for null")
    void shouldThrowExceptionWhenPaymentStatusIsNull() {
        // Arrange
        String orderId = "123";
        EventPayment eventPayment = new EventPayment(
            orderId,
            null,
            LocalDateTime.now()
        );
        
        Order order = new Order(orderId);

        
        when(repositoryOrder.get(orderId)).thenReturn(order);
        
        // Act & Assert
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> paymentProcessingUseCase.invoke(eventPayment)
        );
        
        assertEquals("Status do pagamento não informado!", exception.getMessage());
    }
} 