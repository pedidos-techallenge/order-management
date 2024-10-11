package br.com.fiap.techchallange.ordermanagement.adapters.controllers.checkout;

import br.com.fiap.techchallange.ordermanagement.core.usecase.dto.order.InputDataOrderDTO;
import br.com.fiap.techchallange.ordermanagement.core.usecase.dto.order.OutputDataOrderDTO;

public interface IFinishOrderSelectionController {
    public OutputDataOrderDTO invoke(InputDataOrderDTO inputDataOrderDTO);
}
