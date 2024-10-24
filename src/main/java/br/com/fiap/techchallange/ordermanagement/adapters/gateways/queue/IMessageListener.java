package br.com.fiap.techchallange.ordermanagement.adapters.gateways.queue;

import br.com.fiap.techchallange.ordermanagement.infrastructure.dto.Message;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;

public interface IMessageListener {

    void consumer(String message) throws IOException;
}
