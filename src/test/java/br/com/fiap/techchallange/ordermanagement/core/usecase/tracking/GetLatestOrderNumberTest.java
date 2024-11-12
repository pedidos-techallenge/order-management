package br.com.fiap.techchallange.ordermanagement.core.usecase.tracking;

import br.com.fiap.techchallange.ordermanagement.adapters.gateways.repository.IOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetLatestOrderNumberTest {

//    @Mock
//    private IOrderRepository orderRepository;
//
//    private GetLatestOrderNumber getLatestOrderNumber;
//
//    @BeforeEach
//    void setUp() {
//        getLatestOrderNumber = new GetLatestOrderNumber(orderRepository);
//    }
//
//    @Test
//    void shouldReturnLastNumberFromRepository() {
//        // Arrange
//        int expectedNumber = 42;
//        when(orderRepository.getLastNumber()).thenReturn(expectedNumber);
//
//        // Act
//        int result = getLatestOrderNumber.getLastNumber();
//
//        // Assert
//        assertEquals(expectedNumber, result);
//        verify(orderRepository, times(1)).getLastNumber();
//    }
//
//    @Test
//    void shouldReturnZeroWhenNoOrders() {
//        // Arrange
//        when(orderRepository.getLastNumber()).thenReturn(0);
//
//        // Act
//        int result = getLatestOrderNumber.getLastNumber();
//
//        // Assert
//        assertEquals(0, result);
//        verify(orderRepository, times(1)).getLastNumber();
//    }
//
//    @Test
//    void shouldReturnMaximumNumberWhenMultipleOrders() {
//        // Arrange
//        int expectedNumber = 999999;
//        when(orderRepository.getLastNumber()).thenReturn(expectedNumber);
//
//        // Act
//        int result = getLatestOrderNumber.getLastNumber();
//
//        // Assert
//        assertEquals(expectedNumber, result);
//        verify(orderRepository, times(1)).getLastNumber();
//    }
} 