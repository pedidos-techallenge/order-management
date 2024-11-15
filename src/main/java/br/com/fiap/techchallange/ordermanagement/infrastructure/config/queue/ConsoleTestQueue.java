package br.com.fiap.techchallange.ordermanagement.infrastructure.config.queue;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;

public class ConsoleTestQueue {

    private static String getEnvOrDefault(String key, String defaultValue) {
        String value = System.getenv(key);
        return value != null ? value : defaultValue;
    }

    public static void main(String[] args) {
        // Obter credenciais de variáveis de ambiente
        String accessKey = getEnvOrDefault("AWS_ACCESS_KEY", "test");
        String secretKey = getEnvOrDefault("AWS_SECRET_KEY", "test");
        String region = getEnvOrDefault("AWS_REGION", "us-east-1");
        String queueUrl = getEnvOrDefault("SQS_QUEUE_URL", "http://localhost:4566/000000000000/payment-order-main");

        AmazonSQS sqs = AmazonSQSClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .build();

        String message = "{'idOrder': '55b7cc62-7d5a-4be6-9deb-9da34820a120', 'statusPayment': 'PAID'}";
        SendMessageRequest sendMsgRequest = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody(message);

        SendMessageResult result = sqs.sendMessage(sendMsgRequest);
        System.out.println("Mensagem enviada: " + message);
        System.out.println("Retorno: " + result);
    }
}
