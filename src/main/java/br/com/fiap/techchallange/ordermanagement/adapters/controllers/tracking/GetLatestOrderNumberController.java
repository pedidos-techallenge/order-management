package br.com.fiap.techchallange.ordermanagement.adapters.controllers.tracking;

import br.com.fiap.techchallange.ordermanagement.core.usecase.inputboundary.tracking.IGetLatestOrderNumberUseCase;

public class GetLatestOrderNumberController implements IGetLatestOrderNumberController{

    private final IGetLatestOrderNumberUseCase getLatestOrderNumberUseCase;

    public GetLatestOrderNumberController(IGetLatestOrderNumberUseCase getLatestOrderNumberUseCase){
        this.getLatestOrderNumberUseCase = getLatestOrderNumberUseCase;
    }

    @Override
    public int getLastNumber() {
        return getLatestOrderNumberUseCase.getLastNumber();
    }
}
