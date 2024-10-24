package br.com.fiap.techchallange.ordermanagement.infrastructure.queue;


import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.GetQueueAttributesRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueAttributesResponse;
import software.amazon.awssdk.services.sqs.model.QueueAttributeName;

import java.net.URI;

public class SqsMessageCounter {
    public static void main(String[] args) {
        SqsClient sqsClient = SqsClient.builder()
                .endpointOverride(URI.create("http://localhost:4566"))
                .build();

        String queueUrl = "http://localhost:4566/000000000000/payment-order-main";

        GetQueueAttributesRequest request = GetQueueAttributesRequest.builder()
                .queueUrl(queueUrl)
                .attributeNames(QueueAttributeName.APPROXIMATE_NUMBER_OF_MESSAGES)
                .build();

        GetQueueAttributesResponse response = sqsClient.getQueueAttributes(request);
        String approximateNumberOfMessages = response.attributes().values().toString();

        System.out.println("Número de mensagens não lidas: " + approximateNumberOfMessages);
    }
}
