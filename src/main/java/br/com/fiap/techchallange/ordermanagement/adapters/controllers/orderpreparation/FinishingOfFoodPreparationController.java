package br.com.fiap.techchallange.ordermanagement.adapters.controllers.orderpreparation;

import br.com.fiap.techchallange.ordermanagement.core.usecase.inputboundary.orderpreparation.IFinishingOfFoodPreparationUseCase;

public class FinishingOfFoodPreparationController implements IFinishingOfFoodPreparationController {

    private final IFinishingOfFoodPreparationUseCase finishingOfFoodPreparation;

    public FinishingOfFoodPreparationController(IFinishingOfFoodPreparationUseCase finishingOfFoodPreparation){
        this.finishingOfFoodPreparation = finishingOfFoodPreparation;
    }

    @Override
    public void invoke(int numberOrder) {
        finishingOfFoodPreparation.invoke(numberOrder);
    }
}
