package br.com.fiap.techchallange.ordermanagement.infrastructure.config.controller.checkout;

import br.com.fiap.techchallange.ordermanagement.adapters.controllers.checkout.FinishOrderSelectionController;
import br.com.fiap.techchallange.ordermanagement.adapters.controllers.checkout.IFinishOrderSelectionController;
import br.com.fiap.techchallange.ordermanagement.core.usecase.inputboundary.checkout.IFinishOrderSelectionUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FinishOrderSelectionControllerConfig {

    @Bean
    public IFinishOrderSelectionController getinstanceFinishOrderController(IFinishOrderSelectionUseCase finishOrderSelectionUseCase){
        return new FinishOrderSelectionController(finishOrderSelectionUseCase);
    }
}
