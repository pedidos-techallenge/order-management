package br.com.fiap.techchallange.ordermanagement.core.usecase.tracking;

import br.com.fiap.techchallange.ordermanagement.core.usecase.dto.order.EventOrder;
import br.com.fiap.techchallange.ordermanagement.core.usecase.inputboundary.tracking.IEventListenerOrder;
import br.com.fiap.techchallange.ordermanagement.core.usecase.inputboundary.tracking.IEventTrigger;

public class EventTrigger implements IEventTrigger {
    private final IEventListenerOrder eventListener;

    public EventTrigger(IEventListenerOrder eventListener) {
        this.eventListener = eventListener;
    }

    public void event(EventOrder eventOrder) {
        eventListener.onEvent(eventOrder);
    }
}
