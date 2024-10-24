package br.com.fiap.techchallange.ordermanagement.infrastructure.config.usecase.PaymentProcessing;

import br.com.fiap.techchallange.ordermanagement.adapters.gateways.repository.IOrderRepository;
import br.com.fiap.techchallange.ordermanagement.adapters.gateways.service.IGenerateNumberOrder;
import br.com.fiap.techchallange.ordermanagement.core.usecase.inputboundary.processingpayment.IPaymentProcessingUseCase;
import br.com.fiap.techchallange.ordermanagement.core.usecase.inputboundary.tracking.IEventTrigger;
import br.com.fiap.techchallange.ordermanagement.core.usecase.orderpayment.PaymentProcessingUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentProcessingUseCaseConfig {

    @Bean
    public IPaymentProcessingUseCase getPaymentProcessingUseCaseConfig(IOrderRepository repositoryOrder,
                                                                       IEventTrigger trigger,
                                                                       IGenerateNumberOrder generateNumberOrder){
        return new PaymentProcessingUseCase(repositoryOrder, trigger, generateNumberOrder);

    }
}
