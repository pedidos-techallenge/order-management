package br.com.fiap.techchallange.ordermanagement.core.entity;

import br.com.fiap.techchallange.ordermanagement.core.entity.vo.Item;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderTest {

    @Test
    public void deveCalcularValorTotalDoPedido(){
        //GIVEN
        String idOrder = "fd0b97c0-3334-4c8e-9d83-ae971b77db99";
        float amountExpected = 85;
        int numberOrder      = 1;


        Item item1 = new Item(idOrder, "123456A", 2, 35);
        Item item2 = new Item(idOrder, "123456B", 1, 15);

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
