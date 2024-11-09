package br.com.fiap.techchallange.ordermanagement.infrastructure.config.usecase.checkout;

import br.com.fiap.techchallange.ordermanagement.adapters.gateways.repository.IOrderRepository;
import br.com.fiap.techchallange.ordermanagement.core.usecase.inputboundary.tracking.IGetLatestOrderNumberUseCase;
import br.com.fiap.techchallange.ordermanagement.core.usecase.tracking.GetLatestOrderNumber;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GetLatestOrderNumberUseCaseConfig {

    @Bean
    public IGetLatestOrderNumberUseCase getGetLatestOrderNumberUseCase(IOrderRepository orderRepository){
        return new GetLatestOrderNumber(orderRepository);
    }
}
