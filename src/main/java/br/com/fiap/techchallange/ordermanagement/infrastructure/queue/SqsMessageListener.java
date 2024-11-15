package br.com.fiap.techchallange.ordermanagement.infrastructure.queue;

import br.com.fiap.techchallange.ordermanagement.adapters.gateways.queue.IMessageListener;
import br.com.fiap.techchallange.ordermanagement.core.entity.enums.StatusPayment;
import br.com.fiap.techchallange.ordermanagement.core.entity.vo.EventPayment;
import br.com.fiap.techchallange.ordermanagement.core.usecase.dto.payment.PaymentOrderDTO;
import br.com.fiap.techchallange.ordermanagement.core.usecase.inputboundary.processingpayment.IPaymentProcessingUseCase;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class SqsMessageListener implements IMessageListener {

    @Autowired
    IPaymentProcessingUseCase paymentProcessingUseCase;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @SqsListener("payment-order-main")
    public void consumer(String message) throws IOException {
        System.out.println("Mensagem SQS: {message}" + message);
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        PaymentOrderDTO paymentOrderDTO = objectMapper.readValue(message, PaymentOrderDTO.class);
        StatusPayment statusPayment = StatusPayment.fromValue(paymentOrderDTO.statusPayment());

        EventPayment eventPayment = new EventPayment(paymentOrderDTO.idOrder(), statusPayment, LocalDateTime.now());
        paymentProcessingUseCase.invoke(eventPayment);
    }
}
