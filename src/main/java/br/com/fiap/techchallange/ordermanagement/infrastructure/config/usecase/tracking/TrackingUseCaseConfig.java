package br.com.fiap.techchallange.ordermanagement.infrastructure.config.usecase.tracking;

import br.com.fiap.techchallange.ordermanagement.adapters.gateways.repository.IOrderRepository;
import br.com.fiap.techchallange.ordermanagement.core.usecase.inputboundary.tracking.IEventListenerOrder;
import br.com.fiap.techchallange.ordermanagement.core.usecase.inputboundary.tracking.IGetLatestOrderNumberUseCase;
import br.com.fiap.techchallange.ordermanagement.core.usecase.inputboundary.tracking.IOrderListingUseCase;
import br.com.fiap.techchallange.ordermanagement.core.usecase.outputboundary.presenters.tracking.IDisplayInformationOrderPresenter;
import br.com.fiap.techchallange.ordermanagement.core.usecase.tracking.GetLatestOrderNumber;
import br.com.fiap.techchallange.ordermanagement.core.usecase.tracking.OrderListing;
import br.com.fiap.techchallange.ordermanagement.core.usecase.tracking.OrderUpdateStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TrackingUseCaseConfig {

    @Bean
    public IEventListenerOrder getListenerOrder(IOrderRepository orderRepository,
                                                IDisplayInformationOrderPresenter displayInformationOrderPresenter){
        return new OrderUpdateStatus(orderRepository, displayInformationOrderPresenter);
    }

    @Bean
    public IOrderListingUseCase getOrderListingUseCase(IOrderRepository orderRepository){
        return new OrderListing(orderRepository);
    }

    @Bean
    public IGetLatestOrderNumberUseCase getLatestOrderNumberUseCase(IOrderRepository orderRepository){
        return new GetLatestOrderNumber(orderRepository);
    }
}
