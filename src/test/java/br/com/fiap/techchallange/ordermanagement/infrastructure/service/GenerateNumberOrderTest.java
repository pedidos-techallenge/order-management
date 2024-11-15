package br.com.fiap.techchallange.ordermanagement.infrastructure.service;

import br.com.fiap.techchallange.ordermanagement.adapters.controllers.tracking.IGetLatestOrderNumberController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenerateNumberOrderTest {

    @Mock
    private IGetLatestOrderNumberController getLatestOrderNumberController;

    private GenerateNumberOrder generateNumberOrder;

    @BeforeEach
    void setUp() {
        generateNumberOrder = new GenerateNumberOrder(getLatestOrderNumberController);
    }

    @Test
    @DisplayName("Deve gerar próximo número quando último número for zero")
    void shouldGenerateNextNumberWhenLastNumberIsZero() {
        // Arrange
        when(getLatestOrderNumberController.getLastNumber()).thenReturn(0);

        // Act
        Integer result = generateNumberOrder.generate();

        // Assert
        assertEquals(1, result);
        verify(getLatestOrderNumberController).getLastNumber();
    }

    @Test
    @DisplayName("Deve gerar próximo número quando último número for positivo")
    void shouldGenerateNextNumberWhenLastNumberIsPositive() {
        // Arrange
        when(getLatestOrderNumberController.getLastNumber()).thenReturn(99);

        // Act
        Integer result = generateNumberOrder.generate();

        // Assert
        assertEquals(100, result);
        verify(getLatestOrderNumberController).getLastNumber();
    }

    @Test
    @DisplayName("Deve lançar exceção quando ocorrer overflow")
    void shouldThrowExceptionWhenOverflow() {
        // Arrange
        when(getLatestOrderNumberController.getLastNumber()).thenReturn(Integer.MAX_VALUE);

        // Act & Assert
        ArithmeticException exception = assertThrows(
            ArithmeticException.class,
            () -> generateNumberOrder.generate()
        );
        
        assertEquals("Número máximo de pedidos atingido", exception.getMessage());
        verify(getLatestOrderNumberController).getLastNumber();
    }
} 