package br.com.fiap.techchallange.ordermanagement.adapters.presenters.tracking;

import br.com.fiap.techchallange.ordermanagement.adapters.presenters.viewmodel.OrderViewModel;
import br.com.fiap.techchallange.ordermanagement.core.usecase.dto.order.OutputDataOrderDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderListingPresenterTest {

    private OrderListingPresenter presenter;
    private OutputDataOrderDTO sampleDTO;

    @BeforeEach
    void setUp() {
        presenter = new OrderListingPresenter();
        sampleDTO = new OutputDataOrderDTO(
            "123",
            1,
            "EmPreparacao"
        );
    }

    @Test
    void shouldConvertSingleDTOToViewModel() {
        // When
        OrderViewModel viewModel = presenter.invoke(sampleDTO);

        // Then
        assertNotNull(viewModel);
        assertEquals("123", viewModel.id());
        assertEquals(1, viewModel.number_order());
        assertEquals("EmPreparacao", viewModel.status());
    }

    @Test
    void shouldConvertListOfDTOsToViewModels() {
        // Given
        OutputDataOrderDTO dto1 = new OutputDataOrderDTO("123", 1, "EmPreparacao");
        OutputDataOrderDTO dto2 = new OutputDataOrderDTO("456", 2, "Pronto");
        List<OutputDataOrderDTO> dtoList = Arrays.asList(dto1, dto2);

        // When
        List<OrderViewModel> viewModels = presenter.invoke(dtoList);

        // Then
        assertNotNull(viewModels);
        assertEquals(2, viewModels.size());

        // Verificar primeiro item
        OrderViewModel firstViewModel = viewModels.get(0);
        assertEquals("123", firstViewModel.id());
        assertEquals(1, firstViewModel.number_order());
        assertEquals("EmPreparacao", firstViewModel.status());

        // Verificar segundo item
        OrderViewModel secondViewModel = viewModels.get(1);
        assertEquals("456", secondViewModel.id());
        assertEquals(2, secondViewModel.number_order());
        assertEquals("Pronto", secondViewModel.status());
    }

    @Test
    void shouldHandleEmptyList() {
        // Given
        List<OutputDataOrderDTO> emptyList = List.of();

        // When
        List<OrderViewModel> viewModels = presenter.invoke(emptyList);

        // Then
        assertNotNull(viewModels);
        assertTrue(viewModels.isEmpty());
    }

    @Test
    void shouldCreateNewListInstance() {
        // Given
        List<OutputDataOrderDTO> dtoList = List.of(sampleDTO);

        // When
        List<OrderViewModel> viewModels = presenter.invoke(dtoList);

        // Then
        assertNotNull(viewModels);
        assertNotSame(dtoList, viewModels);
    }
} 