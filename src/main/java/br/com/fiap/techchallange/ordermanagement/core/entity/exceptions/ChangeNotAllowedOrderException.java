package br.com.fiap.techchallange.ordermanagement.core.entity.exceptions;

public class ChangeNotAllowedOrderException extends RuntimeException {

        public ChangeNotAllowedOrderException(String message) {
            super(message);
        }
}
