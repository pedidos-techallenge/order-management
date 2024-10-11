package br.com.fiap.techchallange.ordermanagement.core.usecase.outputboundary.presenters.checkout;

import br.com.fiap.techchallange.ordermanagement.adapters.presenters.viewmodel.OrderViewModel;
import br.com.fiap.techchallange.ordermanagement.core.usecase.dto.order.OutputDataOrderDTO;

public interface IFinishOrderSelectionPresenter {

    public OrderViewModel invoke(OutputDataOrderDTO outputDataOrderDTO);

}
