package br.com.fiap.techchallange.ordermanagement.infrastructure.service.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GenerateNumberOrderMockTest {

    @BeforeEach
    void setUp() {
        SequentialNumberGenerator.getInstance().reset();
    }

    @Test
    void shouldGenerateSequentialNumbers() {
        // Given
        GenerateNumberOrderMock generator = new GenerateNumberOrderMock();
        
        // When
        int firstNumber = generator.generate();
        int secondNumber = generator.generate();
        int thirdNumber = generator.generate();
        
        // Then
        assertEquals(1, firstNumber);
        assertEquals(2, secondNumber);
        assertEquals(3, thirdNumber);
    }

    @Test
    void multipleInstancesShouldShareSameSequence() {
        // Given
        GenerateNumberOrderMock generator1 = new GenerateNumberOrderMock();
        GenerateNumberOrderMock generator2 = new GenerateNumberOrderMock();
        
        // When
        int number1 = generator1.generate();
        int number2 = generator2.generate();
        
        // Then
        assertEquals(1, number1);
        assertEquals(2, number2);
    }
} 