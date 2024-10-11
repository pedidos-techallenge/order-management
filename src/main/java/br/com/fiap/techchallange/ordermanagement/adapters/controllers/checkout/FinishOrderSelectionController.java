package br.com.fiap.techchallange.ordermanagement.adapters.controllers.checkout;

import br.com.fiap.techchallange.ordermanagement.core.usecase.dto.order.InputDataOrderDTO;
import br.com.fiap.techchallange.ordermanagement.core.usecase.dto.order.OutputDataOrderDTO;
import br.com.fiap.techchallange.ordermanagement.core.usecase.inputboundary.checkout.IFinishOrderSelectionUseCase;

public class FinishOrderSelectionController implements IFinishOrderSelectionController {

    public IFinishOrderSelectionUseCase finishOrderSelectionUseCase;

    public FinishOrderSelectionController(IFinishOrderSelectionUseCase finishOrderSelectionUseCase) {
        this.finishOrderSelectionUseCase = finishOrderSelectionUseCase;
    }

    @Override
    public OutputDataOrderDTO invoke(InputDataOrderDTO inputDataOrderDTO) {
       return this.finishOrderSelectionUseCase.invoke(inputDataOrderDTO);
    }
}
