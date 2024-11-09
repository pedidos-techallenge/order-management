package br.com.fiap.techchallange.ordermanagement.infrastructure.repository;

import br.com.fiap.techchallange.ordermanagement.core.entity.Order;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class TestDataLoader {

    @Bean
    public CommandLineRunner loadTestData(MongoTemplate mongoTemplate) {
        return args -> {
            // Limpar dados existentes
            mongoTemplate.dropCollection("test");

            // Inserir dados de teste
            mongoTemplate.save(new Order("defa318d-d6e5-4184-8c15-9c50c4475841", 0,   "Aberto"));
            mongoTemplate.save(new Order("defa318d-d6e5-4184-8c15-9c50c4475842",  10, "Recebido"));
            mongoTemplate.save(new Order("defa318d-d6e5-4184-8c15-9c50c4475843", 11,  "EmPreparacao"));
            mongoTemplate.save(new Order("defa318d-d6e5-4184-8c15-9c50c4475844",  12, "Pronto"));
            mongoTemplate.save(new Order("defa318d-d6e5-4184-8c15-9c50c4475845",  13, "Finalizado"));
        };
    }
}
