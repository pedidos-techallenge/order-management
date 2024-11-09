package br.com.fiap.techchallange.ordermanagement.infrastructure.config.queue;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;

public class ConsoleTestQueue  {

    public static void main(String[] args) {
        String accessKey = "YOUR_ACCESS_KEY";
        String secretKey = "YOUR_SECRET_KEY";
        String region = "us-east-1";
        String queueUrl = "http://localhost:4566/000000000000/payment-order-main";

        AmazonSQS sqs = AmazonSQSClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .build();

        String message =  "{'idOrder': '55b7cc62-7d5a-4be6-9deb-9da34820a120', 'statusPayment': 'PAID'}";
        SendMessageRequest sendMsgRequest = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody(message);

        SendMessageResult result =sqs.sendMessage(sendMsgRequest);
        System.out.println("Mensagem enviada: " + message);
        System.out.println("Retorno: " + result);
    }
}
