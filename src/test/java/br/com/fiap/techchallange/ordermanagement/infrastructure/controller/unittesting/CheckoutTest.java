package br.com.fiap.techchallange.ordermanagement.infrastructure.controller.unittesting;

import br.com.fiap.techchallange.ordermanagement.adapters.controllers.checkout.IFinishOrderSelectionController;
import br.com.fiap.techchallange.ordermanagement.adapters.controllers.orderpreparation.IFinishingOfFoodPreparationController;
import br.com.fiap.techchallange.ordermanagement.adapters.controllers.orderpreparation.IFoodPreparationController;
import br.com.fiap.techchallange.ordermanagement.adapters.presenters.viewmodel.OrderViewModel;
import br.com.fiap.techchallange.ordermanagement.core.usecase.dto.order.InputDataOrderDTO;
import br.com.fiap.techchallange.ordermanagement.core.usecase.dto.order.OutputDataOrderDTO;
import br.com.fiap.techchallange.ordermanagement.core.usecase.outputboundary.presenters.checkout.IFinishOrderSelectionPresenter;

import br.com.fiap.techchallange.ordermanagement.infrastructure.controller.Checkout;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import static br.com.fiap.techchallange.ordermanagement.util.OrderHelper.asJsonString;
import static br.com.fiap.techchallange.ordermanagement.util.OrderHelper.generateOrderDTO;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CheckoutTest {

    MockMvc mockMvc;

    @Mock
    IFinishOrderSelectionController finishOfOrderSelectionController;

    @Mock
    IFinishOrderSelectionPresenter finishOrderSelectionPresenter;

    @Mock
    IFoodPreparationController prepareFood;

    @Mock
    IFinishingOfFoodPreparationController finishPrepareFood;

    AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        Checkout checkout = new Checkout(finishOfOrderSelectionController,
                                         finishOrderSelectionPresenter);
        mockMvc = MockMvcBuilders.standaloneSetup(checkout)
                  .build();
    }

    @AfterEach
    public void tearDown() throws Exception{
        openMocks.close();
    }

    @Nested
    public class Atendimento{

        @Test
        public void deveCriarPedido() throws Exception {
            // GIVEN
            InputDataOrderDTO inputDataOrderDTO = generateOrderDTO();
            OutputDataOrderDTO outputDataOrderDTO = new OutputDataOrderDTO(
                    "fd0b97c0-3334-4c8e-9d83-ae971b77db99",
                    0,
                    "Open"
            );
            OrderViewModel orderViewModel = new OrderViewModel(
                    "fd0b97c0-3334-4c8e-9d83-ae971b77db99",
                    0,
                    "Open"
            );

            when(finishOfOrderSelectionController.invoke(any(InputDataOrderDTO.class)))
                    .thenReturn(outputDataOrderDTO);

            when(finishOrderSelectionPresenter.invoke(any(OutputDataOrderDTO.class)))
                    .thenReturn(orderViewModel);

            // WHEN
            mockMvc.perform(post("/v1/order/checkout")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(inputDataOrderDTO)))
                    .andExpect(status().isCreated());

            // THEN
            verify(finishOrderSelectionPresenter, times(1))
                    .invoke(any(OutputDataOrderDTO.class));
        }
    }
}
