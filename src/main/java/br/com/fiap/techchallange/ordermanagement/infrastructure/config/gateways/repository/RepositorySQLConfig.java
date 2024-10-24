package br.com.fiap.techchallange.ordermanagement.infrastructure.config.gateways.repository;

import br.com.fiap.techchallange.ordermanagement.adapters.gateways.repository.IOrderRepository;
import br.com.fiap.techchallange.ordermanagement.adapters.gateways.repository.IOrderRepositorySQL;
import br.com.fiap.techchallange.ordermanagement.infrastructure.repository.MySQLOrderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
@ConditionalOnProperty(name = "spring.database.sql.enabled", havingValue = "true")
public class RepositorySQLConfig {

    private static final Logger logger = LoggerFactory.getLogger(RepositorySQLConfig.class);
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public RepositorySQLConfig(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Bean
    @Primary
    public IOrderRepositorySQL getOrderRepository(){
        logger.info("Configuration Database SQL MySQL");
        return new MySQLOrderRepository(namedParameterJdbcTemplate);
    }
}


