package br.com.fiap.techchallange.ordermanagement.core.usecase.dto.order;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EventOrderTest {

    @Nested
    class EventOrderRecordTests {
        @Test
        void shouldCreateEventOrderWithAllParameters() {
            // Given & When
            EventOrder event = new EventOrder(1, "process", "123");

            // Then
            assertEquals(1, event.number_order());
            assertEquals("process", event.process());
            assertEquals("123", event.idOrder());
        }

        @Test
        void shouldCreateEventOrderWithTwoParameters() {
            // Given & When
            EventOrder event = new EventOrder(1, "process");

            // Then
            assertEquals(1, event.number_order());
            assertEquals("process", event.process());
            assertNull(event.idOrder());
        }
    }

    @Nested
    class TypeEventOrderTests {
        @Test
        void shouldGetCorrectEnumValue() {
            // Given & When & Then
            assertEquals("paymentApprove", EventOrder.TypeEventOrder.PAYMENTAPPROVE.getValue());
            assertEquals("paymentDenied", EventOrder.TypeEventOrder.PAYMENTDENIED.getValue());
            assertEquals("preparationFood", EventOrder.TypeEventOrder.PREPARATIONFOOD.getValue());
            assertEquals("foodDone", EventOrder.TypeEventOrder.FOODDONE.getValue());
            assertEquals("deliveryFood", EventOrder.TypeEventOrder.DELIVERYFOOD.getValue());
        }

        @Test
        void shouldConvertValidValueToEnum() {
            // Given
            String value = "paymentApprove";

            // When
            EventOrder.TypeEventOrder type = EventOrder.TypeEventOrder.fromValue(value);

            // Then
            assertEquals(EventOrder.TypeEventOrder.PAYMENTAPPROVE, type);
        }

        @Test
        void shouldConvertAllValidValuesToEnum() {
            assertAll(
                () -> assertEquals(EventOrder.TypeEventOrder.PAYMENTAPPROVE,
                                 EventOrder.TypeEventOrder.fromValue("paymentApprove")),
                () -> assertEquals(EventOrder.TypeEventOrder.PAYMENTDENIED,
                                 EventOrder.TypeEventOrder.fromValue("paymentDenied")),
                () -> assertEquals(EventOrder.TypeEventOrder.PREPARATIONFOOD,
                                 EventOrder.TypeEventOrder.fromValue("preparationFood")),
                () -> assertEquals(EventOrder.TypeEventOrder.FOODDONE,
                                 EventOrder.TypeEventOrder.fromValue("foodDone")),
                () -> assertEquals(EventOrder.TypeEventOrder.DELIVERYFOOD,
                                 EventOrder.TypeEventOrder.fromValue("deliveryFood"))
            );
        }

        @Test
        void shouldThrowExceptionForInvalidValue() {
            // Given
            String invalidValue = "invalidValue";

            // When & Then
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> EventOrder.TypeEventOrder.fromValue(invalidValue)
            );

            assertEquals("Valor inválido: " + invalidValue, exception.getMessage());
        }
    }
} 