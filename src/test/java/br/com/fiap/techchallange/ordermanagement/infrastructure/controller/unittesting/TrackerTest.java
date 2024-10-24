package br.com.fiap.techchallange.ordermanagement.infrastructure.controller.unittesting;

import br.com.fiap.techchallange.ordermanagement.adapters.controllers.tracking.IOrderListingController;
import br.com.fiap.techchallange.ordermanagement.adapters.presenters.viewmodel.OrderViewModel;
import br.com.fiap.techchallange.ordermanagement.core.usecase.outputboundary.presenters.tracking.IOrderListingPresenter;
import br.com.fiap.techchallange.ordermanagement.infrastructure.controller.TrackerOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class TrackerTest {

    MockMvc mockMvc;

    @Mock
    IOrderListingPresenter orderListingPresenter;

    @Mock
    IOrderListingController orderListingController;

    AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        TrackerOrder trackerOrder = new TrackerOrder(orderListingPresenter, orderListingController);
        mockMvc = MockMvcBuilders.standaloneSetup(trackerOrder)
                .build();
    }

    @Nested
    public class RastreamentoPedido{

        @Test
        public void deveBuscarListaPedidosEmAtendimento() throws Exception {
            List<OrderViewModel> response = new ArrayList<>();

            when(orderListingPresenter.invoke(orderListingController.invoke())).thenReturn(response);

            // WHEN
            mockMvc.perform(get("/v1/order/tracker")).andExpect(status().isOk());

            // THEN
            verify(orderListingPresenter, times(1)).invoke(orderListingController.invoke());
        }

    }
}
