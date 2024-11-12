package br.com.fiap.techchallange.ordermanagement.core.entity;

import br.com.fiap.techchallange.ordermanagement.core.entity.enums.StatusOrder;
import br.com.fiap.techchallange.ordermanagement.core.entity.exceptions.ChangeNotAllowedOrderException;
import br.com.fiap.techchallange.ordermanagement.core.entity.exceptions.OrderWithoutItemsException;
import br.com.fiap.techchallange.ordermanagement.core.entity.vo.Item;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OrderTest {

    private Order gerarPedido(){

        String idOrder  = "fd0b97c0-3334-4c8e-9d83-ae971b77db99";

        Item item1 = new Item("123456A", 2, 35);
        Item item2 = new Item("123456B", 1, 15);

        List<Item> items = new ArrayList<>();

        items.add(item1);
        items.add(item2);

        return new Order(idOrder, items);
    }

    @Nested
    public class Items{

        @Test
        public void deveGerarPedidoComItens(){
            //GIVEN
            Order order = gerarPedido();

            // WHEN
            List<Item> itens = order.getItems();

            // THEN
            assertThat(itens)
                    .asList()
                    .hasSize(2);

        }

        @Test
        public void deveCriarOrderSemParametro(){
            // GIVEN
            Order order = new Order();

            // WHEN
            String status = order.getStatus();

            //THEN
            assertThat(status).isEqualTo(StatusOrder.OPEN.getValue());
        }

        @Test
        public void deveSetarNumeroOrder(){
            // GIVEN
            Order order = new Order();
            order.setNumberOrder(1);
            // WHEN
            String status = order.getStatus();

            //THEN
            assertThat(status).isEqualTo(StatusOrder.OPEN.getValue());
            assertThat(order.getNumberOrder()).isEqualTo(1);
        }

        @Test
        public void deveCriarOrderComUmParametro(){
            // GIVEN
            Order order = new Order("fd0b97c0-3334-4c8e-9d83-ae971b77db99");

            // WHEN
            String status = order.getStatus();

            //THEN
            assertThat(status).isEqualTo(StatusOrder.OPEN.getValue());
        }

        @Test
        public void deveCriarOrderComTresParametro(){
            // GIVEN
            Order order = new Order("fd0b97c0-3334-4c8e-9d83-ae971b77db99", 0,"Aberto");

            // WHEN
            String status = order.getStatus();

            //THEN
            assertThat(status).isEqualTo(StatusOrder.OPEN.getValue());
        }

        @Test
        public void deveLancarExcecaoDePedidoSemItens() {
            // GIVEN
            List<Item> items = new ArrayList<>();

            // WHEN
            OrderWithoutItemsException thrown = assertThrows(OrderWithoutItemsException.class, () -> {
                new Order("fd0b97c0-3334-4c8e-9d83-ae971b77db99", items);
            });

            // THEN
            assertThat(thrown.getMessage()).isEqualTo("Pedido sem items adicionados!");
        }
    }

    @Nested
    public class Valor{
        @Test
        public void deveCalcularValorTotalDoPedido(){
            //GIVEN
            String idOrder = "fd0b97c0-3334-4c8e-9d83-ae971b77db99";
            float amountExpected = 85;

            Item item1 = new Item("123456A", 2, 35);
            Item item2 = new Item("123456B", 1, 15);

            List<Item> items = new ArrayList<>();

            items.add(item1);
            items.add(item2);

            //WHEN
            Order order = new Order(idOrder, items);
            float amount = order.getAmount();

            //THEN
            assertThat(amount).isEqualTo(amountExpected);
        }
    }


    @Nested
    public class Status{
        @Test
        public void deveAtualizarPedidoParaRecebido(){
            // GIVEN
            Order order = gerarPedido();

            //WHEN
            order.updateStatus(StatusOrder.RECEIVED);

            //THEN
            assertThat(order.getStatus()).isEqualTo("Recebido");
        }

        @Test
        public void deveAtualizarPedidoParaEmPreparacao(){
            // GIVEN
            Order order = gerarPedido();
            order.updateStatus(StatusOrder.RECEIVED);

            // WHEN
            order.updateStatus(StatusOrder.INPREPARATION);

            // THEN
            assertThat(order.getStatus()).isEqualTo("EmPreparacao");
        }

        @Test
        public void deveAtualizarPedidoParaPronto(){
            // GIVEN
            Order order = gerarPedido();

            order.updateStatus(StatusOrder.RECEIVED);
            order.updateStatus(StatusOrder.INPREPARATION);

            // WHEN
            order.updateStatus(StatusOrder.FOODDONE);

            // THEN
            assertThat(order.getStatus()).isEqualTo("Pronto");
        }

        @Test
        public void deveAtualizarPedidoParaFinalizado(){
            // GIVEN
            Order order = gerarPedido();

            order.updateStatus(StatusOrder.RECEIVED);
            order.updateStatus(StatusOrder.INPREPARATION);
            order.updateStatus(StatusOrder.FOODDONE);

            // WHEN
            order.updateStatus(StatusOrder.FINISHED);

            // THEN
            assertThat(order.getStatus()).isEqualTo("Finalizado");
        }

        @Test
        public  void deveLancarExcecaoPorViolacaoDaOrdemDoPedido(){
            // GIVEN
            Order order = gerarPedido();
            order.updateStatus(StatusOrder.RECEIVED);

            // WHEN
            ChangeNotAllowedOrderException thrown = assertThrows(ChangeNotAllowedOrderException.class, () -> {
                order.updateStatus(StatusOrder.FINISHED);
            });

            // THEN
            assertThat(thrown.getMessage()).isEqualTo("Sequencia do status do pedido violado");
        }

        @Test
        public  void deveLancarExcecaoPorStatusNaoPermitido(){
            // GIVEN
            Order order = gerarPedido();

            // WHEN
            ChangeNotAllowedOrderException thrown = assertThrows(ChangeNotAllowedOrderException.class, () -> {
                order.updateStatus(StatusOrder.OPEN);
            });

            // THEN
            assertThat(thrown.getMessage()).isEqualTo("Sequencia do status do pedido violado");
        }

        @Test
        public  void deveLancarExcecaoPorStatusCancelado(){
            // GIVEN
            Order order = gerarPedido();
            order.updateStatus(StatusOrder.CANCELED);

            // WHEN
            ChangeNotAllowedOrderException thrown = assertThrows(ChangeNotAllowedOrderException.class, () -> {
                order.updateStatus(StatusOrder.INPREPARATION);
            });

            // THEN
            assertThat(thrown.getMessage()).isEqualTo("Alteração de status não permitido");
        }
    }

}
