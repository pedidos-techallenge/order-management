package br.com.fiap.techchallange.ordermanagement.core.usecase.outputboundary.presenters.tracking;

import br.com.fiap.techchallange.ordermanagement.adapters.presenters.viewmodel.OrderViewModel;
import br.com.fiap.techchallange.ordermanagement.core.usecase.dto.order.OutputDataOrderDTO;

import java.util.List;

public interface IOrderListingPresenter {
    public List<OrderViewModel> invoke(List<OutputDataOrderDTO> ordersDTO);
}
