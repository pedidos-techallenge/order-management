package br.com.fiap.techchallange.ordermanagement.core.usecase;

import br.com.fiap.techchallange.ordermanagement.adapters.gateways.repository.IOrderRepository;
import br.com.fiap.techchallange.ordermanagement.core.entity.Order;
import br.com.fiap.techchallange.ordermanagement.core.entity.enums.StatusPayment;
import br.com.fiap.techchallange.ordermanagement.core.entity.exceptions.ChangeNotAllowedOrderException;
import br.com.fiap.techchallange.ordermanagement.core.entity.vo.EventPayment;
import br.com.fiap.techchallange.ordermanagement.core.usecase.checkout.FinishOrderSelectionUseCase;
import br.com.fiap.techchallange.ordermanagement.core.usecase.dto.order.EventOrder;
import br.com.fiap.techchallange.ordermanagement.core.usecase.dto.order.InputDataOrderDTO;
import br.com.fiap.techchallange.ordermanagement.core.usecase.dto.order.OutputDataOrderDTO;
import br.com.fiap.techchallange.ordermanagement.core.usecase.finalizationservice.DeliveryOfProductsUseCase;
import br.com.fiap.techchallange.ordermanagement.core.usecase.inputboundary.checkout.IFinishOrderSelectionUseCase;
import br.com.fiap.techchallange.ordermanagement.core.usecase.inputboundary.finalizationservice.IDeliveryOfProductsUseCase;
import br.com.fiap.techchallange.ordermanagement.core.usecase.inputboundary.orderpreparation.IFinishingOfFoodPreparationUseCase;
import br.com.fiap.techchallange.ordermanagement.core.usecase.inputboundary.orderpreparation.IFoodPreparationUseCase;
import br.com.fiap.techchallange.ordermanagement.core.usecase.inputboundary.processingpayment.IPaymentProcessingUseCase;
import br.com.fiap.techchallange.ordermanagement.core.usecase.inputboundary.tracking.IEventTrigger;
import br.com.fiap.techchallange.ordermanagement.core.usecase.inputboundary.tracking.IOrderListingUseCase;
import br.com.fiap.techchallange.ordermanagement.core.usecase.orderpayment.PaymentProcessingUseCase;
import br.com.fiap.techchallange.ordermanagement.core.usecase.orderpreparation.FinishingOfFoodPreparationUseCase;
import br.com.fiap.techchallange.ordermanagement.core.usecase.orderpreparation.FoodPreparationUseCase;
import br.com.fiap.techchallange.ordermanagement.core.usecase.tracking.OrderListing;
import br.com.fiap.techchallange.ordermanagement.infrastructure.service.GenerateNumberOrder;
import br.com.fiap.techchallange.ordermanagement.util.OrderHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static br.com.fiap.techchallange.ordermanagement.util.OrderHelper.generateOrder;
import static br.com.fiap.techchallange.ordermanagement.util.OrderHelper.generateOrderDTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Configuration
public class OrderUseCaseTest {

    private IFinishOrderSelectionUseCase finishSelection;
    private IPaymentProcessingUseCase paymentOrder;
    private IFoodPreparationUseCase foodPreparation;
    private IFinishingOfFoodPreparationUseCase donePreparation;
    private IDeliveryOfProductsUseCase deliveryFood;
    private IOrderListingUseCase orderListing;


    @Mock
    private IEventTrigger trigger;

    @Mock
    private GenerateNumberOrder generateNumberOrder;

    @Mock
    private IOrderRepository orderRepository;

    AutoCloseable openMocks;

    @BeforeEach
    public void setup(){
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception{
        openMocks.close();
    }

    @Nested
    public class Atendimento{
        @Test
        void deveCriarPedido(){
            //GIVEN
            finishSelection = new FinishOrderSelectionUseCase(orderRepository);
            InputDataOrderDTO pedidoDTO = generateOrderDTO();

            //WHEN
            OutputDataOrderDTO dataOrderDTO = finishSelection.invoke(pedidoDTO);

            //THEN
            assertThat(dataOrderDTO.id())
                    .isNotNull()
                    .isEqualTo(pedidoDTO.id());
        }
    }

    @Nested
    public class Pagamento{

        @Test
        void deveAtualizarPedidoParaRecebido() throws IOException {
            // GIVEN
            paymentOrder = new PaymentProcessingUseCase(orderRepository, trigger, generateNumberOrder);

            when(orderRepository.get("fd0b97c0-3334-4c8e-9d83-ae971b77db99")).thenReturn(OrderHelper.generateOrder());
            when(generateNumberOrder.generate()).thenReturn(1);

            // WHEN
            EventPayment eventPayment = new EventPayment("fd0b97c0-3334-4c8e-9d83-ae971b77db99", StatusPayment.PAID, LocalDateTime.now());
            paymentOrder.invoke(eventPayment);

            ArgumentCaptor<EventOrder> eventCaptor = ArgumentCaptor.forClass(EventOrder.class);
            verify(trigger).event(eventCaptor.capture());

            // THEN
            EventOrder eventOrder = eventCaptor.getValue();
            assertThat(eventOrder.process()).isEqualTo(EventOrder.TypeEventOrder.PAYMENTAPPROVE.getValue());

            assertThat(eventOrder.number_order()).isEqualTo(1);
        }

        @Test
        void deveAtualizarPedidoParaCancelado() throws IOException {
            // GIVEN
            paymentOrder = new PaymentProcessingUseCase(orderRepository, trigger, generateNumberOrder);

            when(orderRepository.get("fd0b97c0-3334-4c8e-9d83-ae971b77db99")).thenReturn(OrderHelper.generateOrder());
            when(generateNumberOrder.generate()).thenReturn(1);

            // WHEN
            EventPayment eventPayment = new EventPayment("fd0b97c0-3334-4c8e-9d83-ae971b77db99", StatusPayment.DENIED, LocalDateTime.now());
            paymentOrder.invoke(eventPayment);

            ArgumentCaptor<EventOrder> eventCaptor = ArgumentCaptor.forClass(EventOrder.class);
            verify(trigger).event(eventCaptor.capture());

            // THEN
            EventOrder eventOrder = eventCaptor.getValue();
            assertThat(eventOrder.process()).isEqualTo(EventOrder.TypeEventOrder.PAYMENTDENIED.getValue());

            assertThat(eventOrder.number_order()).isEqualTo(1);
        }
    }

    @Nested
    public class Preparacao{
        @Test
        void deveAtualizarPedidoParaEmPreparacao(){
            // GIVEN
            foodPreparation = new FoodPreparationUseCase(trigger);

            // WHEN
            foodPreparation.invoke(1);

            ArgumentCaptor<EventOrder> eventCaptor = ArgumentCaptor.forClass(EventOrder.class);
            verify(trigger).event(eventCaptor.capture());

            // THEN
            EventOrder eventOrder = eventCaptor.getValue();
            assertThat(eventOrder.process()).isEqualTo(EventOrder.TypeEventOrder.PREPARATIONFOOD.getValue());

            assertThat(eventOrder.number_order()).isEqualTo(1);
        }

        @Test
        void deveLancarExcecaoDeAtualizacaoPedidoCancelado(){
            // GIVEN
            IFoodPreparationUseCase foodPreparation = mock(IFoodPreparationUseCase.class);
            doThrow(new ChangeNotAllowedOrderException("Alteração de status não permitido")).when(foodPreparation).invoke(1);

            // WHEN
            ChangeNotAllowedOrderException thrown = assertThrows(ChangeNotAllowedOrderException.class, () -> {
                foodPreparation.invoke(1);
            });

            // THEN
            assertThat(thrown.getMessage()).isEqualTo("Alteração de status não permitido");
        }

        @Test
        void deveAtualizarPedidoParaPronto(){
            // GIVEN
            donePreparation = new FinishingOfFoodPreparationUseCase(trigger);

            // WHEN
            donePreparation.invoke(1);

            ArgumentCaptor<EventOrder> eventCaptor = ArgumentCaptor.forClass(EventOrder.class);
            verify(trigger).event(eventCaptor.capture());

            // THEN
            EventOrder eventOrder = eventCaptor.getValue();
            assertThat(eventOrder.process()).isEqualTo(EventOrder.TypeEventOrder.FOODDONE.getValue());

            assertThat(eventOrder.number_order()).isEqualTo(1);
        }
    }

    @Nested
    public class Entrega{
        @Test
        void deveAtualizarPedidoParaFinalizado(){
            // GIVEN
            deliveryFood = new DeliveryOfProductsUseCase(trigger);

            // WHEN
            deliveryFood.invoke(1);

            ArgumentCaptor<EventOrder> eventCaptor = ArgumentCaptor.forClass(EventOrder.class);
            verify(trigger).event(eventCaptor.capture());

            // THEN
            EventOrder eventOrder = eventCaptor.getValue();
            assertThat(eventOrder.process()).isEqualTo(EventOrder.TypeEventOrder.DELIVERYFOOD.getValue());

            assertThat(eventOrder.number_order()).isEqualTo(1);
        }
    }

    @Nested
    public class Acompanhamento{
        @Test
        void deveObterUmaListaDePedidos(){
            // GIVEN
            orderListing = new OrderListing(orderRepository);
            List<Order> orders = new ArrayList<>();

            orders.add(OrderHelper.generateOrder());
            orders.add(OrderHelper.generateOrder());
            orders.add(OrderHelper.generateOrder());

            when(orderRepository.getOrders()).thenReturn(orders);

            // WHEN
            List<OutputDataOrderDTO> orderssRetorno = orderListing.invoke();

            //THEN
            assertThat(orderssRetorno)
                    .asList()
                    .hasSize(3);
        }
    }
}
