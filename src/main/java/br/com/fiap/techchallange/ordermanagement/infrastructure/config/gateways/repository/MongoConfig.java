package br.com.fiap.techchallange.ordermanagement.infrastructure.config.gateways.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.annotation.PostConstruct;
import javax.net.ssl.SSLContext;
import java.security.NoSuchAlgorithmException;

@Configuration
public class MongoConfig {

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Value("${spring.data.mongodb.database}")
    private String database;

    private MongoClientSettings createMongoSettings() {
        ConnectionString connectionString = new ConnectionString(mongoUri);
        
        return MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .applyToSslSettings(builder -> {
                builder.enabled(true);
                builder.invalidHostNameAllowed(true);
                try {
                    builder.context(SSLContext.getDefault());
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException("Erro ao configurar SSL", e);
                }
            })
            .build();
    }

    @Bean
    @Profile("dev")
    public MongoClient mongoClientLocal() {
        return MongoClients.create(mongoUri);
    }

    @Bean
    @Profile("prd")
    public MongoClient mongoClientProd() {
        return MongoClients.create(createMongoSettings());
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

