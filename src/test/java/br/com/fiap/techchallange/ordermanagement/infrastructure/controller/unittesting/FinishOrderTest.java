package br.com.fiap.techchallange.ordermanagement.infrastructure.controller.unittesting;

import br.com.fiap.techchallange.ordermanagement.adapters.controllers.orderpreparation.IDeliveryOfProductsController;
import br.com.fiap.techchallange.ordermanagement.core.entity.exceptions.ChangeNotAllowedOrderException;
import br.com.fiap.techchallange.ordermanagement.infrastructure.controller.FinishOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FinishOrderTest {

    MockMvc mockMvc;

    @Mock
    IDeliveryOfProductsController deliveryOfProductsController;

    AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        FinishOrder finishOrder = new FinishOrder(deliveryOfProductsController);
        mockMvc = MockMvcBuilders.standaloneSetup(finishOrder)
                .build();
    }

    @Nested
    public class FinalizacaoPedido{

        @Test
        public void deveColocarStatusPedidoParaFinalizado() throws Exception {
            int order_number = 1;
            doNothing().when(deliveryOfProductsController).invoke(order_number);

            // WHEN
            mockMvc.perform(put("/v1/order/finished/{number_order}", order_number)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isAccepted());

            // THEN
            verify(deliveryOfProductsController, times(1)).invoke(1);
        }

        @Test
        public void deveLancarExcecaoMudancaDeStatusNaoPermitida() throws Exception {
            int order_number = 1;
            doThrow(ChangeNotAllowedOrderException.class).when(deliveryOfProductsController).invoke(order_number);

            // WHEN
            mockMvc.perform(put("/v1/order/finished/{number_order}", order_number)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            // THEN
            verify(deliveryOfProductsController, times(1)).invoke(1);
        }
    }
}
