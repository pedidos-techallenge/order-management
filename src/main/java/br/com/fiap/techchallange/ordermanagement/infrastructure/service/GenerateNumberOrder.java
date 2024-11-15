package br.com.fiap.techchallange.ordermanagement.infrastructure.service;

import br.com.fiap.techchallange.ordermanagement.adapters.controllers.tracking.IGetLatestOrderNumberController;
import br.com.fiap.techchallange.ordermanagement.adapters.gateways.service.IGenerateNumberOrder;

public class GenerateNumberOrder implements IGenerateNumberOrder {

    private final IGetLatestOrderNumberController getLatestOrderNumberController;

    public GenerateNumberOrder(IGetLatestOrderNumberController getLatestOrderNumberController) {
        this.getLatestOrderNumberController = getLatestOrderNumberController;
    }

    @Override
    public Integer generate() {
        int lastNumber = getLatestOrderNumberController.getLastNumber();
        try {
            return Math.addExact(lastNumber, 1);
        } catch (ArithmeticException e) {
            throw new ArithmeticException("Número máximo de pedidos atingido");
        }
    }
}
