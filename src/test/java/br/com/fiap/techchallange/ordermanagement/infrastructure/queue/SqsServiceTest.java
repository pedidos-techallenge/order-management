package br.com.fiap.techchallange.ordermanagement.infrastructure.queue;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

public class SqsServiceTest {

    @Mock
    private SqsTemplate sqsTemplate;

    @InjectMocks
    private SqsService sqsService;

    public SqsServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSendMessage() {
        // Arrange
        String message = "Teste de mensagem";

        // Act
        sqsService.sendMessage(message);

        // Assert
        verify(sqsTemplate).send(message); // Verifica se o método send foi chamado com a mensagem correta
    }
}
