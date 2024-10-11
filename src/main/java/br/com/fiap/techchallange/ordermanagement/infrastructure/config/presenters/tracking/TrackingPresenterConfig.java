package br.com.fiap.techchallange.ordermanagement.infrastructure.config.presenters.tracking;

import br.com.fiap.techchallange.ordermanagement.adapters.gateways.service.IDisplayMonitor;
import br.com.fiap.techchallange.ordermanagement.adapters.presenters.tracking.DisplayInformationOrderPresenter;
import br.com.fiap.techchallange.ordermanagement.adapters.presenters.tracking.OrderListingPresenter;
import br.com.fiap.techchallange.ordermanagement.core.usecase.outputboundary.presenters.tracking.IDisplayInformationOrderPresenter;
import br.com.fiap.techchallange.ordermanagement.core.usecase.outputboundary.presenters.tracking.IOrderListingPresenter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TrackingPresenterConfig {

    @Bean
    public IDisplayInformationOrderPresenter getDisplayInformationOrderPresenter(IDisplayMonitor displayMonitor){
        return new DisplayInformationOrderPresenter(displayMonitor);
    }

    @Bean
    public IOrderListingPresenter getOrderListingPresenter(){
        return new OrderListingPresenter();
    }
}
