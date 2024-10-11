package br.com.fiap.techchallange.ordermanagement.core.usecase.inputboundary.tracking;

import br.com.fiap.techchallange.ordermanagement.core.usecase.dto.order.EventOrder;

public interface IEventTrigger {
    public void event(EventOrder eventOrder);
}
