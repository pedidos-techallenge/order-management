package br.com.fiap.techchallange.ordermanagement.core.entity.exceptions;

public class OrderWithoutItemsException extends RuntimeException {
    public OrderWithoutItemsException(String message) {
        super(message);
    }
}
