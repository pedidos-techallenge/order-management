package br.com.fiap.techchallange.ordermanagement.core.usecase.outputboundary.presenters.tracking;

import br.com.fiap.techchallange.ordermanagement.core.usecase.dto.order.OutputDataOrderDTO;

public interface IDisplayInformationOrderPresenter {
    public void display(OutputDataOrderDTO orderDTO);
}
