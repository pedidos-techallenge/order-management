package br.com.fiap.techchallange.ordermanagement.infrastructure.gateways;

import br.com.fiap.techchallange.ordermanagement.adapters.gateways.service.IGenerateNumberOrder;
import org.springframework.stereotype.Service;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class GenerateNumberOrderImpl implements IGenerateNumberOrder {
    
    private final AtomicInteger counter = new AtomicInteger(1);

    @Override
    public Integer generate() {
        return counter.getAndIncrement();
    }
} 