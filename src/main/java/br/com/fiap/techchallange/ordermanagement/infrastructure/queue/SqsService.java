package br.com.fiap.techchallange.ordermanagement.infrastructure.queue;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
public class SqsService {

    @Autowired
    private SqsTemplate sqsTemplate;

    public void sendMessage(String message) {
        sqsTemplate.send(message);
        System.out.println("Mensagem enviada para a fila SQS: " + message);
    }
}
