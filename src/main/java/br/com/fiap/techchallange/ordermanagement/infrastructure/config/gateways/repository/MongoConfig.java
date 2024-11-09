package br.com.fiap.techchallange.ordermanagement.infrastructure.config.gateways.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import com.mongodb.client.MongoClients;

import jakarta.annotation.PostConstruct;

import com.mongodb.client.MongoClient;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
public class MongoConfig {

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Value("${spring.data.mongodb.database}")
    private String database;

    @Bean
    @Profile("dev")
    public MongoClient mongoClientLocal() {
        return MongoClients.create(mongoUri);
    }

    @Bean
    @Profile("prd")
    public MongoClient mongoClientProd() {
        return MongoClients.create(mongoUri);
    }

    @Bean
    public MongoTemplate mongoTemplate(@Autowired MongoClient mongoClient) {
        return new MongoTemplate(mongoClient, database);
    }

    @PostConstruct
    public void logMongoUri() {
        String sanitizedUri = mongoUri.replaceAll(":[^:@]+@", ":****@");
        System.out.println("MongoDB URI: " + sanitizedUri);
    }

}

