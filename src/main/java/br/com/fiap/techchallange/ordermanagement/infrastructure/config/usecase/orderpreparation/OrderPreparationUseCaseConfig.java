package br.com.fiap.techchallange.ordermanagement.infrastructure.config.usecase.orderpreparation;

import br.com.fiap.techchallange.ordermanagement.core.usecase.inputboundary.orderpreparation.IFinishingOfFoodPreparationUseCase;
import br.com.fiap.techchallange.ordermanagement.core.usecase.inputboundary.orderpreparation.IFoodPreparationUseCase;
import br.com.fiap.techchallange.ordermanagement.core.usecase.inputboundary.tracking.IEventTrigger;
import br.com.fiap.techchallange.ordermanagement.core.usecase.orderpreparation.FinishingOfFoodPreparationUseCase;
import br.com.fiap.techchallange.ordermanagement.core.usecase.orderpreparation.FoodPreparationUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderPreparationUseCaseConfig {

    @Bean
    public IFoodPreparationUseCase foodPreparationUseCase(IEventTrigger trigger){
        return new FoodPreparationUseCase(trigger);
    }

    @Bean
    public IFinishingOfFoodPreparationUseCase finishingOfFoodPreparationUseCase(IEventTrigger trigger){
        return new FinishingOfFoodPreparationUseCase(trigger);
    }
}
