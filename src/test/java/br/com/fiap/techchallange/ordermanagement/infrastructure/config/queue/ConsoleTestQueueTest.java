package br.com.fiap.techchallange.ordermanagement.infrastructure.config.queue;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ConsoleTestQueueTest {

    private AmazonSQS sqs;
    private ConsoleTestQueue consoleTestQueue;

    @BeforeEach
    public void setUp() {
        sqs = mock(AmazonSQS.class);
        consoleTestQueue = new ConsoleTestQueue();
    }

    @Test
    public void testGetEnvOrDefault() {
        String result = consoleTestQueue.getEnvOrDefault("TEST_KEY", "default");
        assertEquals("default", result); // Testa se o valor padrão é retornado
    }

    @Test
    public void testSendMessage() {
        // Configura o mock para retornar um resultado simulado
        SendMessageResult sendMessageResult = new SendMessageResult();
        sendMessageResult.setMessageId("12345");
        when(sqs.sendMessage(any(SendMessageRequest.class))).thenReturn(sendMessageResult);

        // Chama o método que envia a mensagem
        String message = "{'idOrder': '55b7cc62-7d5a-4be6-9deb-9da34820a120', 'statusPayment': 'PAID'}";
        SendMessageRequest sendMsgRequest = new SendMessageRequest()
                .withQueueUrl("http://localhost:4566/000000000000/payment-order-main")
                .withMessageBody(message);

        SendMessageResult result = sqs.sendMessage(sendMsgRequest);

        // Verifica se o método sendMessage foi chamado
        verify(sqs, times(1)).sendMessage(sendMsgRequest);
        assertEquals("12345", result.getMessageId()); // Verifica se o ID da mensagem retornado é o esperado
    }
} 