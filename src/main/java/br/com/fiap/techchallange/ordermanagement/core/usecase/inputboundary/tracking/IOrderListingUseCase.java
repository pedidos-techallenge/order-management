package br.com.fiap.techchallange.ordermanagement.core.usecase.inputboundary.tracking;

import br.com.fiap.techchallange.ordermanagement.core.usecase.dto.order.OutputDataOrderDTO;

import java.util.List;

public interface IOrderListingUseCase {

    public List<OutputDataOrderDTO> invoke();
    public OutputDataOrderDTO invoke(String idOrder);
}
