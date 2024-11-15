package br.com.fiap.techchallange.ordermanagement.core.entity.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    @Test
    @DisplayName("Deve criar um item com valores válidos")
    void shouldCreateItemWithValidValues() {
        // Arrange & Act
        Item item = new Item("SKU123", 2, 10.5f);

        // Assert
        assertEquals("SKU123", item.getSku());
        assertEquals(2, item.getQuantity());
        assertEquals(10.5f, item.getUnitValue());
        assertEquals(21.0f, item.getAmount());
    }

    @ParameterizedTest
    @DisplayName("Deve lançar exceção quando quantidade for menor ou igual a zero")
    @ValueSource(ints = {0, -1, -5})
    void shouldThrowExceptionWhenQuantityIsZeroOrNegative(int invalidQuantity) {
        // Arrange & Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Item("SKU123", invalidQuantity, 10.5f)
        );

        assertEquals(
            "quantity of item cannot be less than or equal to zero",
            exception.getMessage()
        );
    }

    @Test
    @DisplayName("Deve calcular valor total corretamente")
    void shouldCalculateTotalAmountCorrectly() {
        // Arrange & Act
        Item item = new Item("SKU123", 3, 15.5f);

        // Assert
        assertEquals(46.5f, item.getAmount(), 0.001f);
    }

    @Test
    @DisplayName("Deve criar item com valor unitário zero")
    void shouldCreateItemWithZeroUnitValue() {
        // Arrange & Act
        Item item = new Item("SKU123", 1, 0f);

        // Assert
        assertEquals(0f, item.getUnitValue());
        assertEquals(0f, item.getAmount());
    }

    @Test
    @DisplayName("Deve criar item com valor unitário negativo")
    void shouldCreateItemWithNegativeUnitValue() {
        // Arrange & Act
        Item item = new Item("SKU123", 2, -10.5f);

        // Assert
        assertEquals(-10.5f, item.getUnitValue());
        assertEquals(-21.0f, item.getAmount());
    }
} 