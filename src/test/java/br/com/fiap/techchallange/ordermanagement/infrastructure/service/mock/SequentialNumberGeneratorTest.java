package br.com.fiap.techchallange.ordermanagement.infrastructure.service.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class SequentialNumberGeneratorTest {

    private SequentialNumberGenerator generator;

    @BeforeEach
    void setUp() {
        SequentialNumberGenerator.getInstance().reset();
        generator = SequentialNumberGenerator.getInstance();
    }

    @Test
    void shouldBeSingleton() {
        // When
        SequentialNumberGenerator instance1 = SequentialNumberGenerator.getInstance();
        SequentialNumberGenerator instance2 = SequentialNumberGenerator.getInstance();
        
        // Then
        assertSame(instance1, instance2);
    }

    @Test
    void shouldGenerateSequentialNumbers() {
        // When
        int firstNumber = generator.generateNextNumber();
        int secondNumber = generator.generateNextNumber();
        int thirdNumber = generator.generateNextNumber();
        
        // Then
        assertEquals(1, firstNumber);
        assertEquals(2, secondNumber);
        assertEquals(3, thirdNumber);
    }

    @Test
    void shouldMaintainSequenceAcrossMultipleCalls() {
        // When & Then
        for (int i = 1; i <= 5; i++) {
            int number = generator.generateNextNumber();
            assertEquals(i, number, "Número gerado deve ser " + i);
        }
    }

    @Test
    void shouldFormatCurrentNumber() {
        // When
        generator.generateNextNumber(); // gera número 1
        String formattedNumber = generator.getFormattedCurrentNumber();
        
        // Then
        assertEquals("00001", formattedNumber);
    }

    @Test
    void shouldThrowExceptionWhenLimitReached() {
        // When & Then
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            for (int i = 0; i <= 10000; i++) {
                generator.generateNextNumber();
            }
        });
        
        assertEquals("Limite máximo de números atingido", exception.getMessage());
    }

    @Test
    void shouldResetToZero() {
        // Given
        generator.generateNextNumber(); // gera 1
        generator.generateNextNumber(); // gera 2
        
        // When
        generator.reset();
        
        // Then
        assertEquals(1, generator.generateNextNumber());
    }
} 