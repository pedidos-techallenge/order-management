package br.com.fiap.techchallange.ordermanagement.adapters.presenters.tracking;

import br.com.fiap.techchallange.ordermanagement.adapters.presenters.viewmodel.OrderViewModel;
import br.com.fiap.techchallange.ordermanagement.core.usecase.dto.order.OutputDataOrderDTO;
import br.com.fiap.techchallange.ordermanagement.core.usecase.outputboundary.presenters.tracking.IOrderListingPresenter;

import java.util.ArrayList;
import java.util.List;

public class OrderListingPresenter implements IOrderListingPresenter {

    @Override
    public List<OrderViewModel> invoke(List<OutputDataOrderDTO> ordersDTO) {

        List<OrderViewModel> ordersView = new ArrayList<>();

        for(OutputDataOrderDTO outputDataOrderDTO: ordersDTO){
            ordersView.add(new OrderViewModel(outputDataOrderDTO.id(),
                                              outputDataOrderDTO.number_order(),
                                              outputDataOrderDTO.status()));
        }

        return ordersView;
    }
}
