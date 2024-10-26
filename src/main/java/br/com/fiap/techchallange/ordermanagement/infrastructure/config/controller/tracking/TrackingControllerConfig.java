package br.com.fiap.techchallange.ordermanagement.infrastructure.config.controller.tracking;

import br.com.fiap.techchallange.ordermanagement.adapters.controllers.tracking.GetLatestOrderNumberController;
import br.com.fiap.techchallange.ordermanagement.adapters.controllers.tracking.IGetLatestOrderNumberController;
import br.com.fiap.techchallange.ordermanagement.adapters.controllers.tracking.IOrderListingController;
import br.com.fiap.techchallange.ordermanagement.adapters.controllers.tracking.OrderListingController;
import br.com.fiap.techchallange.ordermanagement.core.usecase.inputboundary.tracking.IGetLatestOrderNumberUseCase;
import br.com.fiap.techchallange.ordermanagement.core.usecase.inputboundary.tracking.IOrderListingUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TrackingControllerConfig {

    @Bean
    public IOrderListingController getOrderListingController(IOrderListingUseCase orderListingUseCase){
        return new OrderListingController(orderListingUseCase);
    }

    @Bean
    public IGetLatestOrderNumberController getLatestOrderNumberController(IGetLatestOrderNumberUseCase getLatestOrderNumberUseCase){
        return new GetLatestOrderNumberController(getLatestOrderNumberUseCase);
    }
}
