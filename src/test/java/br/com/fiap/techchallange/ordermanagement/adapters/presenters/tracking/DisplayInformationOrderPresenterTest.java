package br.com.fiap.techchallange.ordermanagement.adapters.presenters.tracking;

import br.com.fiap.techchallange.ordermanagement.adapters.gateways.service.IDisplayMonitor;
import br.com.fiap.techchallange.ordermanagement.adapters.presenters.viewmodel.OrderViewModel;
import br.com.fiap.techchallange.ordermanagement.core.usecase.dto.order.OutputDataOrderDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DisplayInformationOrderPresenterTest {

    @Mock
    private IDisplayMonitor displayMonitor;

    private DisplayInformationOrderPresenter presenter;

    @BeforeEach
    void setUp() {
        presenter = new DisplayInformationOrderPresenter(displayMonitor);
    }

    @Test
    void shouldDisplayOrderInformation() {
        // Given
        OutputDataOrderDTO orderDTO = new OutputDataOrderDTO(
            "123",
            1,
            "EmPreparacao"
        );

        // When
        presenter.display(orderDTO);

        // Then
        ArgumentCaptor<OrderViewModel> viewModelCaptor = ArgumentCaptor.forClass(OrderViewModel.class);
        verify(displayMonitor).display(viewModelCaptor.capture());

        OrderViewModel capturedViewModel = viewModelCaptor.getValue();
        assertEquals("123", capturedViewModel.id());
        assertEquals(1, capturedViewModel.number_order());
        assertEquals("EmPreparacao", capturedViewModel.status());
    }

    @Test
    void shouldCallDisplayMonitorExactlyOnce() {
        // Given
        OutputDataOrderDTO orderDTO = new OutputDataOrderDTO(
            "123",
            1,
            "EmPreparacao"
        );

        // When
        presenter.display(orderDTO);

        // Then
        verify(displayMonitor, times(1)).display(any(OrderViewModel.class));
    }

    @Test
    void shouldCreateNewViewModelInstance() {
        // Given
        OutputDataOrderDTO orderDTO = new OutputDataOrderDTO(
            "123",
            1,
            "EmPreparacao"
        );

        // When
        presenter.display(orderDTO);
        presenter.display(orderDTO);

        // Then
        ArgumentCaptor<OrderViewModel> viewModelCaptor = ArgumentCaptor.forClass(OrderViewModel.class);
        verify(displayMonitor, times(2)).display(viewModelCaptor.capture());

        List<OrderViewModel> capturedViewModels = viewModelCaptor.getAllValues();
        assertNotSame(capturedViewModels.get(0), capturedViewModels.get(1));
    }

    @Test
    void shouldHandleNullFields() {
        // Given
        OutputDataOrderDTO orderDTO = new OutputDataOrderDTO(
            null,
            0,
            null
        );

        // When
        presenter.display(orderDTO);

        // Then
        ArgumentCaptor<OrderViewModel> viewModelCaptor = ArgumentCaptor.forClass(OrderViewModel.class);
        verify(displayMonitor).display(viewModelCaptor.capture());

        OrderViewModel capturedViewModel = viewModelCaptor.getValue();
        assertNull(capturedViewModel.id());
        assertEquals(0, capturedViewModel.number_order());
        assertNull(capturedViewModel.status());
    }
} 