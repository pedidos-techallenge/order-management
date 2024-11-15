package br.com.fiap.techchallange.ordermanagement.core.entity.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class StatusPaymentTest {

    @ParameterizedTest
    @DisplayName("Deve retornar o valor correto para cada status")
    @CsvSource({
        "PAID,PAID",
        "DENIED,DENIED"
    })
    void shouldReturnCorrectValue(StatusPayment status, String expectedValue) {
        assertEquals(expectedValue, status.getValue());
    }

    @ParameterizedTest
    @DisplayName("Deve converter string válida para enum corretamente")
    @CsvSource({
        "PAID,PAID",
        "DENIED,DENIED"
    })
    void shouldConvertValidStringToEnum(String value, StatusPayment expectedStatus) {
        assertEquals(expectedStatus, StatusPayment.fromValue(value));
    }

    @ParameterizedTest
    @DisplayName("Deve lançar exceção para valores inválidos")
    @ValueSource(strings = {"", "PENDING", "APPROVED", "REJECTED", "paid", "denied"})
    void shouldThrowExceptionForInvalidValues(String invalidValue) {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> StatusPayment.fromValue(invalidValue)
        );
        
        assertEquals("Valor inválido: " + invalidValue, exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando valor for null")
    void shouldThrowExceptionWhenValueIsNull() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> StatusPayment.fromValue(null)
        );
        
        assertEquals("Valor inválido: null", exception.getMessage());
    }

    @Test
    @DisplayName("Deve conter todos os status esperados")
    void shouldContainAllExpectedStatus() {
        StatusPayment[] statuses = StatusPayment.values();
        
        assertEquals(2, statuses.length);
        assertTrue(containsStatus(statuses, "PAID"));
        assertTrue(containsStatus(statuses, "DENIED"));
    }

    private boolean containsStatus(StatusPayment[] statuses, String value) {
        for (StatusPayment status : statuses) {
            if (status.getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }
} 