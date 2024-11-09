package br.com.fiap.techchallange.ordermanagement.bdd;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CucumberSpringConfig {

    @Bean
    public SharedData sharedData() {
        return new SharedData();
    }
}
