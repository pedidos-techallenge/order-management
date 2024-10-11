package br.com.fiap.techchallange.ordermanagement.infrastructure.config.usecase.checkout;


import br.com.fiap.techchallange.ordermanagement.adapters.gateways.repository.IOrderRepository;
import br.com.fiap.techchallange.ordermanagement.core.usecase.checkout.FinishOrderSelectionUseCase;
import br.com.fiap.techchallange.ordermanagement.core.usecase.inputboundary.checkout.IFinishOrderSelectionUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IFinishOrderSelectionUseCaseConfig {

    @Bean
    public IFinishOrderSelectionUseCase getinstanceFinishOrderUseCase(IOrderRepository orderRepository){
        return new FinishOrderSelectionUseCase(orderRepository);
    }
}
