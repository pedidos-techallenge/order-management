package br.com.fiap.techchallange.ordermanagement.infrastructure.api;

import br.com.fiap.techchallange.ordermanagement.adapters.controllers.checkout.IFinishOrderSelectionController;
import br.com.fiap.techchallange.ordermanagement.adapters.presenters.viewmodel.ErrorViewModel;
import br.com.fiap.techchallange.ordermanagement.adapters.presenters.viewmodel.OrderViewModel;
import br.com.fiap.techchallange.ordermanagement.core.entity.exceptions.ChangeNotAllowedOrderException;
import br.com.fiap.techchallange.ordermanagement.core.usecase.dto.order.InputDataItemDTO;
import br.com.fiap.techchallange.ordermanagement.core.usecase.dto.order.InputDataOrderDTO;
import br.com.fiap.techchallange.ordermanagement.core.usecase.outputboundary.presenters.checkout.IFinishOrderSelectionPresenter;
import br.com.fiap.techchallange.ordermanagement.infrastructure.dto.ItemRequestDTO;
import br.com.fiap.techchallange.ordermanagement.infrastructure.dto.OrderRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v1/checkout")
@Tag(name = "2. Checkout", description = "Endpoints para a criação de pedido do cliente.")
public class Checkout {


    IFinishOrderSelectionController finishOfOrderSelectionController;
    IFinishOrderSelectionPresenter finishOrderSelectionPresenter;

    public Checkout(
                    IFinishOrderSelectionController finishOfOrderSelectionController,
                    IFinishOrderSelectionPresenter finishOrderSelectionPresenter){

        this.finishOfOrderSelectionController = finishOfOrderSelectionController;
        this.finishOrderSelectionPresenter = finishOrderSelectionPresenter;
    }

    @Operation(summary = "Envia o pedido para cadastro base de dados.")
    @PostMapping("/orders")
    public ResponseEntity<?> registerOrder(@RequestBody OrderRequestDTO orderRequest) throws EmptyResultDataAccessException, DuplicateKeyException {
        try {
            List<InputDataItemDTO> itemsInput = new ArrayList<>();
            for(ItemRequestDTO item: orderRequest.getItems()){
                itemsInput.add(new InputDataItemDTO(item.sku(), item.amount(), item.quantity()));
            }

            OrderViewModel response = this.finishOrderSelectionPresenter.invoke(this.finishOfOrderSelectionController.invoke(new InputDataOrderDTO(orderRequest.getId(), itemsInput)));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>(new ErrorViewModel(2,"Ocorreu um problema ao registrar a order de serviço"), HttpStatus.BAD_REQUEST);
        } catch (DuplicateKeyException e) {
            return new ResponseEntity<>(new ErrorViewModel(3,"Este order id já existe no banco"), HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler(ChangeNotAllowedOrderException.class)
    public ResponseEntity<ErrorViewModel> handleChangeNotAllowedOrderException(ChangeNotAllowedOrderException ex) {
        ErrorViewModel error = new ErrorViewModel(4, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
