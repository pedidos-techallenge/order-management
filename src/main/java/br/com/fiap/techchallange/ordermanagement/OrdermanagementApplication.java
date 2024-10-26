package br.com.fiap.techchallange.ordermanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@ComponentScan(basePackages = {"br.com.fiap.techchallange.ordermanagement.infrastructure.adapters",
							   "br.com.fiap.techchallange.ordermanagement.infrastructure.factory",
							   "br.com.fiap.techchallange.ordermanagement.infrastructure.config",
								"br.com.fiap.techchallange.ordermanagement.infrastructure.config.repository",
							   "br.com.fiap.techchallange.ordermanagement.adapters.gateways.repository",
							   "br.com.fiap.techchallange.ordermanagement.infrastructure.repository",
							   "br.com.fiap.techchallange.ordermanagement.infrastructure.controller",
							   "br.com.fiap.techchallange.ordermanagement.infrastructure.queue"
} )
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class OrdermanagementApplication {

	public static void main(String[] args) {
		System.out.println("Environment: " + System.getenv("SPRING_PROFILES_ACTIVE"));
		SpringApplication.run(OrdermanagementApplication.class, args);
	}

}
