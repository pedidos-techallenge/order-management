package br.com.fiap.techchallange.ordermanagement.infrastructure.config.controller.paymentorder;

import br.com.fiap.techchallange.ordermanagement.adapters.controllers.tracking.GetLatestOrderNumberController;
import br.com.fiap.techchallange.ordermanagement.adapters.controllers.tracking.IGetLatestOrderNumberController;
import br.com.fiap.techchallange.ordermanagement.core.usecase.inputboundary.tracking.IGetLatestOrderNumberUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GetLatestOrderNumberControllerConfig {

    @Bean
    public IGetLatestOrderNumberController getGetLatestOrderNumberController(IGetLatestOrderNumberUseCase getLatestOrderNumberUseCase){
        return new GetLatestOrderNumberController(getLatestOrderNumberUseCase);
    }
}
