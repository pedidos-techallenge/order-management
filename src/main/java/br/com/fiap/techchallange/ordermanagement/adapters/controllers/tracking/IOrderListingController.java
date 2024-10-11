package br.com.fiap.techchallange.ordermanagement.adapters.controllers.tracking;

import br.com.fiap.techchallange.ordermanagement.core.usecase.dto.order.OutputDataOrderDTO;

import java.util.List;

public interface IOrderListingController {
    public List<OutputDataOrderDTO> invoke();
}
