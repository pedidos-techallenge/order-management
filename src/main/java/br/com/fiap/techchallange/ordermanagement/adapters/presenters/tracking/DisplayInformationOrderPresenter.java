package br.com.fiap.techchallange.ordermanagement.adapters.presenters.tracking;

import br.com.fiap.techchallange.ordermanagement.adapters.gateways.service.IDisplayMonitor;
import br.com.fiap.techchallange.ordermanagement.adapters.presenters.viewmodel.OrderViewModel;
import br.com.fiap.techchallange.ordermanagement.core.usecase.dto.order.OutputDataOrderDTO;
import br.com.fiap.techchallange.ordermanagement.core.usecase.outputboundary.presenters.tracking.IDisplayInformationOrderPresenter;

public class DisplayInformationOrderPresenter implements IDisplayInformationOrderPresenter {

    private final IDisplayMonitor displayMonitor;

    public DisplayInformationOrderPresenter(IDisplayMonitor displayMonitor){
        this.displayMonitor = displayMonitor;
    }

    @Override
    public void display(OutputDataOrderDTO orderDTO) {
        this.displayMonitor.display(new OrderViewModel(orderDTO.id(),
                                                       orderDTO.number_order(),
                                                       orderDTO.status()));
    }
}
