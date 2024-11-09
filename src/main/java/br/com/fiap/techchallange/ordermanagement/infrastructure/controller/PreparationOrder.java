package br.com.fiap.techchallange.ordermanagement.infrastructure.controller;

import br.com.fiap.techchallange.ordermanagement.adapters.controllers.orderpreparation.IFinishingOfFoodPreparationController;
import br.com.fiap.techchallange.ordermanagement.adapters.controllers.orderpreparation.IFoodPreparationController;
import br.com.fiap.techchallange.ordermanagement.adapters.presenters.viewmodel.ErrorViewModel;
import br.com.fiap.techchallange.ordermanagement.core.entity.exceptions.ChangeNotAllowedOrderException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1/order/operation")
@Tag(name = "2. Order Preparation", description = "Endpoints para preparação do pedido.")
public class PreparationOrder {

    private final IFoodPreparationController prepareFood;
    private final IFinishingOfFoodPreparationController finishPrepareFood;

    public PreparationOrder(IFinishingOfFoodPreparationController finishPrepareFood,
                            IFoodPreparationController controllerPrepare){
        this.finishPrepareFood = finishPrepareFood;
        this.prepareFood = controllerPrepare;
    }

    @Operation(summary = "Coloca o pedido no status de preparação da comida")
    @PutMapping("/preparation/{number_order}")
    public ResponseEntity<Map<String, String>> prepareFoodResponse(@PathVariable int number_order){
        prepareFood.invoke(number_order);
        Map<String, String> response = new HashMap<>();
        response.put("status", "Pedido em preparação");
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @Operation(summary = "Coloca o pedido no status de preparação da comida pronta")
    @PutMapping("/done/{number_order}")
    public ResponseEntity<Map<String, String>> finishpreParationResponse(@PathVariable int number_order){
        finishPrepareFood.invoke(number_order);
        Map<String, String> response = new HashMap<>();
        response.put("status", "Pedido pronto para entrega ao cliente!");
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @ExceptionHandler(ChangeNotAllowedOrderException.class)
    public ResponseEntity<ErrorViewModel> handleChangeNotAllowedOrderException(ChangeNotAllowedOrderException ex) {
        ErrorViewModel error = new ErrorViewModel(3256, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
