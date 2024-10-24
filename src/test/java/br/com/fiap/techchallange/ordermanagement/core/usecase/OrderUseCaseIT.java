package br.com.fiap.techchallange.ordermanagement.core.usecase;

import br.com.fiap.techchallange.ordermanagement.adapters.gateways.repository.IOrderRepository;
import br.com.fiap.techchallange.ordermanagement.core.entity.Order;
import br.com.fiap.techchallange.ordermanagement.core.entity.enums.StatusOrder;
import br.com.fiap.techchallange.ordermanagement.core.entity.enums.StatusPayment;
import br.com.fiap.techchallange.ordermanagement.core.entity.exceptions.ChangeNotAllowedOrderException;
import br.com.fiap.techchallange.ordermanagement.core.entity.vo.EventPayment;
import br.com.fiap.techchallange.ordermanagement.core.usecase.checkout.FinishOrderSelectionUseCase;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

import static br.com.fiap.techchallange.ordermanagement.util.OrderHelper.generateOrderDTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
@ActiveProfiles("test")
@ComponentScan
public class OrderUseCaseIT {

    private IFinishOrderSelectionUseCase finishSelection;
    private IPaymentProcessingUseCase paymentOrder;
    private IFoodPreparationUseCase foodPreparation;
    private IFinishingOfFoodPreparationUseCase donePreparation;
    private IDeliveryOfProductsUseCase deliveryFood;
    private IOrderListingUseCase orderListing;


    @Autowired
    private IEventTrigger trigger;

    @Autowired
    private GenerateNumberOrder generateNumberOrder;

    @Autowired
    private IOrderRepository orderRepository;

    @BeforeEach
    public void setup(){
        finishSelection = new FinishOrderSelectionUseCase(orderRepository);
        paymentOrder    = new PaymentProcessingUseCase(orderRepository, trigger, generateNumberOrder);
        foodPreparation = new FoodPreparationUseCase(trigger);
        donePreparation = new FinishingOfFoodPreparationUseCase(trigger);
        deliveryFood    = new DeliveryOfProductsUseCase(trigger);
        orderListing    = new OrderListing(orderRepository);
        orderRepository.clearDB();
    }

    @Nested
    public class Atendimento{
        @Test
        void deveCriarPedido(){
            //GIVEN
            InputDataOrderDTO pedidoDTO = generateOrderDTO();

            //WHEN
            OutputDataOrderDTO orderDTO = finishSelection.invoke(pedidoDTO);
            Order order = orderRepository.get(orderDTO.id());

            //THEN
            assertThat(order.getId())
                    .isNotNull()
                    .isEqualTo(orderDTO.id());
        }
    }

    @Nested
    public class Pagamento{
        @Test
        void deveRealizarPagamentoDoPedido() throws IOException {
            // GIVEN
            InputDataOrderDTO pedidoDTO = generateOrderDTO();
            OutputDataOrderDTO dataOrderDTO = finishSelection.invoke(pedidoDTO);


            // WHEN
            EventPayment eventPayment = new EventPayment(dataOrderDTO.id(), StatusPayment.PAID);
            paymentOrder.invoke(eventPayment);
            Order order = orderRepository.get(dataOrderDTO.id());

            // THEN
            assertThat(order.getStatus()).isEqualTo(StatusOrder.RECEIVED.getValue());
        }

        @Test
        void deveCancelarPedidoPorPagamentoRecusado() throws IOException {
            // GIVEN
            InputDataOrderDTO pedidoDTO = generateOrderDTO();
            OutputDataOrderDTO dataOrderDTO = finishSelection.invoke(pedidoDTO);


            // WHEN
            EventPayment eventPayment = new EventPayment(dataOrderDTO.id(), StatusPayment.DENIED);
            paymentOrder.invoke(eventPayment);
            Order order = orderRepository.get(dataOrderDTO.id());

            // THEN
            assertThat(order.getStatus()).isEqualTo(StatusOrder.CANCELED.getValue());
        }
    }

    @Nested
    public class Preparacao{
        @Test
        void deveAtualizarPedidoParaEmPreparacao() throws IOException {
            // GIVEN
            InputDataOrderDTO pedidoDTO = generateOrderDTO();
            OutputDataOrderDTO dataOrderDTO = finishSelection.invoke(pedidoDTO);

            EventPayment eventPayment = new EventPayment(dataOrderDTO.id(), StatusPayment.PAID);
            paymentOrder.invoke(eventPayment);

            // WHEN
            foodPreparation.invoke(1);
            Order order = orderRepository.get(dataOrderDTO.id());

            // THEN
            assertThat(order.getStatus()).isEqualTo(StatusOrder.INPREPARATION.getValue());
        }

        @Test
        void deveAtualizarPedidoParaPronto() throws IOException {
            // GIVEN
            InputDataOrderDTO pedidoDTO = generateOrderDTO();
            OutputDataOrderDTO dataOrderDTO = finishSelection.invoke(pedidoDTO);

            EventPayment eventPayment = new EventPayment(dataOrderDTO.id(), StatusPayment.PAID);
            paymentOrder.invoke(eventPayment);
            foodPreparation.invoke(1);

            // WHEN
            donePreparation.invoke(1);
            Order order = orderRepository.get(dataOrderDTO.id());

            // THEN
            assertThat(order.getStatus()).isEqualTo(StatusOrder.FOODDONE.getValue());
        }

        @Test
        void deveLancarErroDeAtualizacaoPagamentoCancelado() throws IOException {
            // GIVEN
            InputDataOrderDTO pedidoDTO = generateOrderDTO();
            OutputDataOrderDTO dataOrderDTO = finishSelection.invoke(pedidoDTO);

            EventPayment eventPayment = new EventPayment(dataOrderDTO.id(), StatusPayment.DENIED);
            paymentOrder.invoke(eventPayment);

            // WHEN
            ChangeNotAllowedOrderException thrown = assertThrows(ChangeNotAllowedOrderException.class, () -> {
                foodPreparation.invoke(1);
            });

            // THEN
            assertThat(thrown.getMessage()).isEqualTo("Alteração de status não permitido");
        }
    }

    @Nested
    public class Entrega{
        @Test
        void deveAtualizarPedidoParaFinalizado() throws IOException {
            // GIVEN
            InputDataOrderDTO pedidoDTO = generateOrderDTO();
            OutputDataOrderDTO dataOrderDTO = finishSelection.invoke(pedidoDTO);

            EventPayment eventPayment = new EventPayment(dataOrderDTO.id(), StatusPayment.PAID);
            paymentOrder.invoke(eventPayment);

            foodPreparation.invoke(1);
            donePreparation.invoke(1);

            // WHEN
            deliveryFood.invoke(1);

            Order order = orderRepository.get(dataOrderDTO.id());

            // THEN
            assertThat(order.getStatus()).isEqualTo(StatusOrder.FINISHED.getValue());
        }
    }

    @Nested
    public class Acompanhamento{
        @Test
        void deveObterUmaListaDePedidosEmAndamento() throws IOException {
            // GIVEN
            InputDataOrderDTO pedido1 = generateOrderDTO();
            InputDataOrderDTO pedido2 = generateOrderDTO();
            InputDataOrderDTO pedido3 = generateOrderDTO();
            InputDataOrderDTO pedido4 = generateOrderDTO();

            finishSelection.invoke(pedido1);
            finishSelection.invoke(pedido2);
            finishSelection.invoke(pedido3);
            finishSelection.invoke(pedido4);

            EventPayment eventPayment1 = new EventPayment(pedido1.id(), StatusPayment.PAID);
            EventPayment eventPayment2 = new EventPayment(pedido2.id(), StatusPayment.PAID);
            EventPayment eventPayment3 = new EventPayment(pedido3.id(), StatusPayment.DENIED);

            paymentOrder.invoke(eventPayment1);
            paymentOrder.invoke(eventPayment2);
            paymentOrder.invoke(eventPayment3);


            // WHEN
            List<OutputDataOrderDTO> orderssRetorno = orderListing.invoke();

            //THEN
            assertThat(orderssRetorno)
                    .asList()
                    .hasSize(2);
        }
    }
}
