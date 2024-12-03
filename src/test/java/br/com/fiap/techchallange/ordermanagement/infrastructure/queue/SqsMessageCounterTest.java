package br.com.fiap.techchallange.ordermanagement.infrastructure.queue;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.GetQueueAttributesRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueAttributesResponse;
import software.amazon.awssdk.services.sqs.model.QueueAttributeName;

import java.util.HashMap;
import java.util.Map;
import static org.mockito.Mockito.*;

public class SqsMessageCounterTest {

    @Test
    public void testMain() {
        // Mock do SqsClient
        SqsClient sqsClient = mock(SqsClient.class);
        
        // Configurando o comportamento do mock
        Map<QueueAttributeName, String> attributes = new HashMap<>();
        attributes.put(QueueAttributeName.APPROXIMATE_NUMBER_OF_MESSAGES, "5");

        GetQueueAttributesResponse response = GetQueueAttributesResponse.builder()
                .attributes(attributes)
                .build();

        when(sqsClient.getQueueAttributes(any(GetQueueAttributesRequest.class))).thenReturn(response);

        // Substituindo o SqsClient na classe SqsMessageCounter
        SqsMessageCounter.setSqsClient(sqsClient); // Você precisará adicionar um método setSqsClient na sua classe

        // Executando o método main
        SqsMessageCounter.main(new String[]{});

        // Verificando se o método foi chamado corretamente
        verify(sqsClient).getQueueAttributes(any(GetQueueAttributesRequest.class));
    }
}