package br.com.fiap.techchallange.ordermanagement.core.usecase.orderpayment;

import br.com.fiap.techchallange.ordermanagement.adapters.gateways.repository.IOrderRepository;
import br.com.fiap.techchallange.ordermanagement.adapters.gateways.service.IGenerateNumberOrder;
import br.com.fiap.techchallange.ordermanagement.core.entity.Order;
import br.com.fiap.techchallange.ordermanagement.core.entity.enums.StatusPayment;
import br.com.fiap.techchallange.ordermanagement.core.entity.vo.EventPayment;
import br.com.fiap.techchallange.ordermanagement.core.usecase.dto.order.EventOrder;
import br.com.fiap.techchallange.ordermanagement.core.usecase.inputboundary.processingpayment.IPaymentProcessingUseCase;
import br.com.fiap.techchallange.ordermanagement.core.usecase.inputboundary.tracking.IEventTrigger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class PaymentProcessingUseCase implements IPaymentProcessingUseCase {
    private final IOrderRepository repositoryOrder;
    private final IEventTrigger trigger;
    private final IGenerateNumberOrder generateNumberOrder;

    public PaymentProcessingUseCase(IOrderRepository repositoryOrder, IEventTrigger trigger, IGenerateNumberOrder generateNumberOrder) {
        this.repositoryOrder = repositoryOrder;
        this.trigger = trigger;
        this.generateNumberOrder = generateNumberOrder;
    }

    @Override
    public void invoke(EventPayment eventPayment) throws IOException {
        Order order = repositoryOrder.get(eventPayment.idOrder());
        EventOrder.TypeEventOrder typeEvent = null;

        typeEvent = getTypeEventOrder(eventPayment);

        // order.getNumberOrder() é atualizado antes do evento ser disparado
        order.setNumberOrder(generateNumberOrder.generate());
        repositoryOrder.update(order);

        this.trigger.event(new EventOrder(
                order.getNumberOrder(),
                typeEvent.getValue(),
                order.getId()
        ));
    }

    private static EventOrder.@NotNull TypeEventOrder getTypeEventOrder(EventPayment eventPayment) {
        EventOrder.TypeEventOrder typeEvent;
        if (eventPayment.statusPayment() == StatusPayment.PAID)
            typeEvent = EventOrder.TypeEventOrder.PAYMENTAPPROVE;
        else if (eventPayment.statusPayment() == StatusPayment.DENIED) {
            typeEvent = EventOrder.TypeEventOrder.PAYMENTDENIED;
        }else {
            throw new RuntimeException("Status do pagamento não informado!");
        }
        return typeEvent;
    }
}
