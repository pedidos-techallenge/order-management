package br.com.fiap.techchallange.ordermanagement.core.entity.vo;

import br.com.fiap.techchallange.ordermanagement.core.entity.enums.StatusPayment;

public record EventPayment(String idOrder, StatusPayment statusPayment, java.time.LocalDateTime now) {
}
