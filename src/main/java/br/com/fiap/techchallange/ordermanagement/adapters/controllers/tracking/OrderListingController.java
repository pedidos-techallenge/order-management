package br.com.fiap.techchallange.ordermanagement.adapters.controllers.tracking;

import br.com.fiap.techchallange.ordermanagement.core.usecase.dto.order.OutputDataOrderDTO;
import br.com.fiap.techchallange.ordermanagement.core.usecase.inputboundary.tracking.IOrderListingUseCase;

import java.util.List;

public class OrderListingController implements IOrderListingController {

    private final IOrderListingUseCase orderListingUseCase;

    public OrderListingController(IOrderListingUseCase orderListingUseCase){
        this.orderListingUseCase = orderListingUseCase;
    }

    @Override
    public List<OutputDataOrderDTO> invoke() {
        return orderListingUseCase.invoke();
    }
}
