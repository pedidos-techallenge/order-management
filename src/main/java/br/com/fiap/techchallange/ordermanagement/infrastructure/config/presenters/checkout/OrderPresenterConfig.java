package br.com.fiap.techchallange.ordermanagement.infrastructure.config.presenters.checkout;

import br.com.fiap.techchallange.ordermanagement.adapters.presenters.checkout.FinishOrderSelectionPresenter;
import br.com.fiap.techchallange.ordermanagement.core.usecase.outputboundary.presenters.checkout.IFinishOrderSelectionPresenter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderPresenterConfig {

    @Bean
    public IFinishOrderSelectionPresenter getFinishOrderSelectionPresenter(){
        return new FinishOrderSelectionPresenter();
    }
}
