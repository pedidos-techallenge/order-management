package br.com.fiap.techchallange.ordermanagement.infrastructure.controller.unittesting;

import br.com.fiap.techchallange.ordermanagement.adapters.controllers.orderpreparation.IFinishingOfFoodPreparationController;
import br.com.fiap.techchallange.ordermanagement.adapters.controllers.orderpreparation.IFoodPreparationController;
import br.com.fiap.techchallange.ordermanagement.core.entity.exceptions.ChangeNotAllowedOrderException;
import br.com.fiap.techchallange.ordermanagement.infrastructure.controller.PreparationOrder;
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

public class PreparationTest {

    MockMvc mockMvc;

    @Mock
    IFoodPreparationController prepareFood;

    @Mock
    IFinishingOfFoodPreparationController finishPrepareFood;

    AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        PreparationOrder prepartionOrder = new PreparationOrder(finishPrepareFood, prepareFood);
        mockMvc = MockMvcBuilders.standaloneSetup(prepartionOrder)
                .build();
    }

    @Nested
    public class PreparacaoPedido{

        @Test
        public void deveColocarStatusPedidoEmPreparacao() throws Exception {
            int order_number = 1;
            doNothing().when(prepareFood).invoke(order_number);

            // WHEN
            mockMvc.perform(put("/v1/order/operation/preparation/{number_order}", order_number)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isAccepted());

            // THEN
            verify(prepareFood, times(1)).invoke(1);
        }

        @Test
        public void deveColocarStatusPedidoParaComidaPronta() throws Exception {
            int order_number = 1;
            doNothing().when(finishPrepareFood).invoke(order_number);

            // WHEN
            mockMvc.perform(put("/v1/order/operation/done/{number_order}", order_number)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isAccepted());

            // THEN
            verify(finishPrepareFood, times(1)).invoke(1);
        }

        @Test
        public void deveLancarExcecaoMudancaDeStatusNaoPermitida() throws Exception {
            int order_number = 1;
            doThrow(ChangeNotAllowedOrderException.class).when(finishPrepareFood).invoke(order_number);

            // WHEN
            mockMvc.perform(put("/v1/order/operation/done/{number_order}", order_number)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            // THEN
            verify(finishPrepareFood, times(1)).invoke(1);
        }
    }
}
