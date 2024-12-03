package br.com.fiap.techchallange.ordermanagement.infrastructure.queue;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.GetQueueAttributesRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueAttributesResponse;
import software.amazon.awssdk.services.sqs.model.QueueAttributeName;

import java.net.URI;

public class SqsMessageCounter {
    private static SqsClient sqsClient;

    public static void setSqsClient(SqsClient client) {
        sqsClient = client;
    }

    public static void main(String[] args) {
        if (sqsClient == null) {
            sqsClient = SqsClient.builder()
                    .endpointOverride(URI.create("http://localhost:4566"))
                    .credentialsProvider(DefaultCredentialsProvider.create())
                    .build();
        }

        String queueUrl = "http://localhost:4566/000000000000/payment-order-main";

        GetQueueAttributesRequest request = GetQueueAttributesRequest.builder()
                .queueUrl(queueUrl)
                .attributeNames(QueueAttributeName.APPROXIMATE_NUMBER_OF_MESSAGES)
                .build();

        GetQueueAttributesResponse response = sqsClient.getQueueAttributes(request);
        String approximateNumberOfMessages = response.attributes().get(QueueAttributeName.APPROXIMATE_NUMBER_OF_MESSAGES);

        System.out.println("Número de mensagens não lidas: " + approximateNumberOfMessages);
    }
}
