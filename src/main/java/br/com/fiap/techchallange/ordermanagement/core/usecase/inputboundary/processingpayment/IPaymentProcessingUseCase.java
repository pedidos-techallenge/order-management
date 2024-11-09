package br.com.fiap.techchallange.ordermanagement.core.usecase.inputboundary.processingpayment;

import br.com.fiap.techchallange.ordermanagement.core.entity.vo.EventPayment;

import java.io.IOException;

public interface IPaymentProcessingUseCase {
    void invoke(EventPayment eventPayment) throws IOException;
}
