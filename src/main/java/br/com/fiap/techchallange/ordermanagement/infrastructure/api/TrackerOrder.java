package br.com.fiap.techchallange.ordermanagement.infrastructure.api;

import br.com.fiap.techchallange.ordermanagement.adapters.controllers.tracking.IOrderListingController;
import br.com.fiap.techchallange.ordermanagement.adapters.presenters.viewmodel.ErrorViewModel;
import br.com.fiap.techchallange.ordermanagement.adapters.presenters.viewmodel.OrderViewModel;
import br.com.fiap.techchallange.ordermanagement.core.entity.exceptions.ChangeNotAllowedOrderException;
import br.com.fiap.techchallange.ordermanagement.core.usecase.outputboundary.presenters.tracking.IOrderListingPresenter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/tracker/orders")
@Tag(name = "7. Tracker Order", description = "Endpoints para o rastreamento dos pedidos.")
public class TrackerOrder {

    private final IOrderListingPresenter orderListingPresenter;
    private final IOrderListingController orderListingController;

    public TrackerOrder(IOrderListingPresenter orderListingPresenter,
                        IOrderListingController orderListingController){
        this.orderListingController = orderListingController;
        this.orderListingPresenter = orderListingPresenter;
    }

    @Operation(summary = "Busca os pedidos que estão no processo de atendimento")
    @GetMapping("/list")
    public ResponseEntity<List<OrderViewModel>> getOrders(){
        List<OrderViewModel> response = orderListingPresenter.invoke(orderListingController.invoke());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler(ChangeNotAllowedOrderException.class)
    public ResponseEntity<ErrorViewModel> handleChangeNotAllowedOrderException(ChangeNotAllowedOrderException ex) {
        ErrorViewModel error = new ErrorViewModel(4, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

}
