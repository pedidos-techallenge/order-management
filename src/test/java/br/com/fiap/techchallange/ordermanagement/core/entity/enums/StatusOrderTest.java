package br.com.fiap.techchallange.ordermanagement.core.entity.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class StatusOrderTest {

    @ParameterizedTest
    @DisplayName("Deve retornar o valor correto para cada status")
    @CsvSource({
        "OPEN,Aberto",
        "RECEIVED,Recebido",
        "INPREPARATION,EmPreparacao",
        "FOODDONE,Pronto",
        "FINISHED,Finalizado",
        "CANCELED,Cancelado"
    })
    void shouldReturnCorrectValue(StatusOrder status, String expectedValue) {
        assertEquals(expectedValue, status.getValue());
    }

    @ParameterizedTest
    @DisplayName("Deve converter string válida para enum corretamente")
    @CsvSource({
        "Aberto,OPEN",
        "Recebido,RECEIVED",
        "EmPreparacao,INPREPARATION",
        "Pronto,FOODDONE",
        "Finalizado,FINISHED",
        "Cancelado,CANCELED"
    })
    void shouldConvertValidStringToEnum(String value, StatusOrder expectedStatus) {
        assertEquals(expectedStatus, StatusOrder.fromValue(value));
    }

    @ParameterizedTest
    @DisplayName("Deve lançar exceção para valores inválidos")
    @ValueSource(strings = {"", "Invalid", "WRONG", "Em Preparacao", "Preparando"})
    void shouldThrowExceptionForInvalidValues(String invalidValue) {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> StatusOrder.fromValue(invalidValue)
        );
        
        assertEquals("Valor inválido: " + invalidValue, exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando valor for null")
    void shouldThrowExceptionWhenValueIsNull() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> StatusOrder.fromValue(null)
        );
        
        assertEquals("Valor inválido: null", exception.getMessage());
    }

    @Test
    @DisplayName("Deve conter todos os status esperados")
    void shouldContainAllExpectedStatus() {
        StatusOrder[] statuses = StatusOrder.values();
        
        assertEquals(6, statuses.length);
        assertTrue(containsStatus(statuses, "Aberto"));
        assertTrue(containsStatus(statuses, "Recebido"));
        assertTrue(containsStatus(statuses, "EmPreparacao"));
        assertTrue(containsStatus(statuses, "Pronto"));
        assertTrue(containsStatus(statuses, "Finalizado"));
        assertTrue(containsStatus(statuses, "Cancelado"));
    }

    private boolean containsStatus(StatusOrder[] statuses, String value) {
        for (StatusOrder status : statuses) {
            if (status.getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }
} 