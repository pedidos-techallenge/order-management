package br.com.fiap.techchallange.ordermanagement.infrastructure.service;

import br.com.fiap.techchallange.ordermanagement.adapters.presenters.viewmodel.OrderViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DisplayMonitorWebTest {

    @Mock
    private RestTemplate restTemplate;

    private DisplayMonitorWeb displayMonitorWeb;
    
    private static final String TEST_MONITOR_URL = "http://test-monitor/notify";

    @BeforeEach
    void setUp() {
        displayMonitorWeb = new DisplayMonitorWeb(restTemplate, TEST_MONITOR_URL);
    }

    @Test
    @DisplayName("Deve criar instância com construtor padrão")
    void shouldCreateInstanceWithDefaultConstructor() {
        // Act
        DisplayMonitorWeb monitor = new DisplayMonitorWeb();
        
        // Assert
        assertNotNull(monitor);
    }

    @Test
    @DisplayName("Deve enviar notificação com sucesso")
    void shouldSendNotificationSuccessfully() {
        // Arrange
        OrderViewModel orderView = new OrderViewModel("1", 1, "PRONTO");
        DisplayMonitorWeb.OrderNotification notification = new DisplayMonitorWeb.OrderNotification(1, "PRONTO");
        
        when(restTemplate.postForEntity(
            TEST_MONITOR_URL,
            notification,
            Void.class
        )).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        displayMonitorWeb.display(orderView);

        // Assert
        verify(restTemplate).postForEntity(
            TEST_MONITOR_URL,
            notification,
            Void.class
        );
    }

    @Test
    @DisplayName("Deve lidar com erro de conexão")
    void shouldHandleConnectionError() {
        // Arrange
        OrderViewModel orderView = new OrderViewModel("1", 1, "PRONTO");
        DisplayMonitorWeb.OrderNotification notification = new DisplayMonitorWeb.OrderNotification(1, "PRONTO");
        
        when(restTemplate.postForEntity(
            TEST_MONITOR_URL,
            notification,
            Void.class
        )).thenThrow(new RestClientException("Erro de conexão"));

        // Act & Assert
        assertThrows(RestClientException.class, () -> 
            displayMonitorWeb.display(orderView)
        );
    }

    @Test
    @DisplayName("Deve lidar com resposta de erro do servidor")
    void shouldHandleServerError() {
        // Arrange
        OrderViewModel orderView = new OrderViewModel("1", 1, "PRONTO");
        DisplayMonitorWeb.OrderNotification notification = new DisplayMonitorWeb.OrderNotification(1, "PRONTO");
        
        when(restTemplate.postForEntity(
            TEST_MONITOR_URL,
            notification,
            Void.class
        )).thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));

        // Act
        displayMonitorWeb.display(orderView);

        // Assert
        verify(restTemplate).postForEntity(
            TEST_MONITOR_URL,
            notification,
            Void.class
        );
    }
} 