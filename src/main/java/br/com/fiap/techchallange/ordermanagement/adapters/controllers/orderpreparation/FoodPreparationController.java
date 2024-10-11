package br.com.fiap.techchallange.ordermanagement.adapters.controllers.orderpreparation;

import br.com.fiap.techchallange.ordermanagement.core.usecase.inputboundary.orderpreparation.IFoodPreparationUseCase;

public class FoodPreparationController implements IFoodPreparationController {

    private final IFoodPreparationUseCase foodPreparation;

    public FoodPreparationController(IFoodPreparationUseCase foodPreparation){
        this.foodPreparation = foodPreparation;
    }

    @Override
    public void invoke(int numberOrder) {
        foodPreparation.invoke(numberOrder);
    }
}
