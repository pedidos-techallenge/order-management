package br.com.fiap.techchallange.ordermanagement.infrastructure.gateways;

import br.com.fiap.techchallange.ordermanagement.adapters.presenters.viewmodel.OrderViewModel;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;

import static org.mockito.Mockito.*;

public class DisplayMonitorImplTest {

    @Test
    public void testDisplay() {
        // Cria um mock do Logger
        Logger loggerMock = Mockito.mock(Logger.class);
        
        // Cria uma instância do DisplayMonitorImpl com o mock do logger
        DisplayMonitorImpl displayMonitor = new DisplayMonitorImpl(loggerMock);

        // Cria um OrderViewModel de exemplo
        OrderViewModel orderView = new OrderViewModel("teste-123", 1, "Open"); // Preencha com dados de teste se necessário

        // Chama o método display
        displayMonitor.display(orderView);

        // Verifica se o logger foi chamado com a mensagem correta
        verify(loggerMock).info("Display Monitor: {}", orderView);
    }
} 