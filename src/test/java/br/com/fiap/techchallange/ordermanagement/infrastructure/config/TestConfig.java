package br.com.fiap.techchallange.ordermanagement.infrastructure.config;

import br.com.fiap.techchallange.ordermanagement.adapters.gateways.service.IDisplayMonitor;
import br.com.fiap.techchallange.ordermanagement.adapters.gateways.service.IGenerateNumberOrder;
import com.mongodb.MongoClientSettings;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.ConnectionString;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.testcontainers.containers.MongoDBContainer;

@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public IDisplayMonitor displayMonitor() {
        return Mockito.mock(IDisplayMonitor.class);
    }

    @Bean
    @Primary
    public IGenerateNumberOrder generateNumberOrder() {
        return Mockito.mock(IGenerateNumberOrder.class);
    }

    @Bean
    @Primary
    public SqsAsyncClient sqsAsyncClient() {
        return Mockito.mock(SqsAsyncClient.class);
    }

    @Bean
    @Primary
    public SqsTemplate sqsTemplate(SqsAsyncClient sqsAsyncClient) {
        return SqsTemplate.builder()
                .sqsAsyncClient(sqsAsyncClient)
                .build();
    }

    @Bean
    @Primary
    public MongoClient mongoClient(MongoDBContainer mongoDBContainer) {
        return MongoClients.create(MongoClientSettings.builder()
            .applyConnectionString(new ConnectionString(
                String.format("mongodb://%s:%d",
                    mongoDBContainer.getHost(),
                    mongoDBContainer.getMappedPort(27017))
            ))
            .build());
    }

    @Bean
    @Primary
    public MongoTemplate testMongoTemplate(MongoClient mongoClient) {
        return new MongoTemplate(mongoClient, "test");
    }
} 