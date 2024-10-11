package br.com.fiap.techchallange.ordermanagement.adapters.controllers.orderpreparation;

import br.com.fiap.techchallange.ordermanagement.core.usecase.inputboundary.finalizationservice.IDeliveryOfProductsUseCase;

public class DeliveryOfProductsController implements IDeliveryOfProductsController {

    private final IDeliveryOfProductsUseCase deliveryOfProducts;

    public DeliveryOfProductsController(IDeliveryOfProductsUseCase deliveryOfProducts){
        this.deliveryOfProducts = deliveryOfProducts;
    }

    @Override
    public void invoke(int numberOrder) {
        deliveryOfProducts.invoke(numberOrder);
    }
}
