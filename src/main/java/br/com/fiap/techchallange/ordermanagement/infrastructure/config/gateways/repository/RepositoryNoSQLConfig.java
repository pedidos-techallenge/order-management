package br.com.fiap.techchallange.ordermanagement.infrastructure.config.gateways.repository;

import br.com.fiap.techchallange.ordermanagement.adapters.gateways.repository.IOrderRepositoryNoSQL;
import br.com.fiap.techchallange.ordermanagement.infrastructure.repository.DocumentDBOrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@ConditionalOnProperty(name = "spring.database.sql.enabled", havingValue = "false")
public class RepositoryNoSQLConfig {

    private static final Logger logger = LoggerFactory.getLogger(RepositoryNoSQLConfig.class);

    @Bean
    @Primary
    public IOrderRepositoryNoSQL getOrderRepository(){
        logger.info("Configuration Database NoSQL MongoDB");
        return new DocumentDBOrderRepository();
    }
}
