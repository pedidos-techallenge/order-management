package br.com.fiap.techchallange.ordermanagement.util;

import br.com.fiap.techchallange.ordermanagement.core.entity.Order;
import br.com.fiap.techchallange.ordermanagement.core.entity.enums.StatusOrder;
import br.com.fiap.techchallange.ordermanagement.core.entity.vo.Item;
import br.com.fiap.techchallange.ordermanagement.core.usecase.dto.order.InputDataItemDTO;
import br.com.fiap.techchallange.ordermanagement.core.usecase.dto.order.InputDataOrderDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class OrderHelper {

    public static InputDataOrderDTO generateOrderDTO(){
        String idOrder  = UUID.randomUUID().toString();

        List<InputDataItemDTO> items = new ArrayList<>();

        items.add(new InputDataItemDTO("123456A", 2, 35));
        items.add(new InputDataItemDTO("123456B", 1, 15));

        return new InputDataOrderDTO(idOrder, items);
    }

    public static Order generateOrder(){

        Item item1 = new Item("123456A", 2, 35);
        Item item2 = new Item("123456B", 1, 15);

        List<Item> items = new ArrayList<>();

        items.add(item1);
        items.add(item2);

        return new Order(UUID.randomUUID().toString(),  items);
    }

    public static Order generateOrder(int numberOrder, StatusOrder status){
        String idOrder  = "fd0b97c0-3334-4c8e-9d83-ae971b77db99";
        Random random = new Random();


        Item item1 = new Item("123456A", 2, 35);
        Item item2 = new Item("123456B", 1, 15);

        List<Item> items = new ArrayList<>();

        items.add(item1);
        items.add(item2);

        return new Order(idOrder, numberOrder, items, status.getValue());
    }

    public static Order generateOrder(String id, int numberOrder, StatusOrder status){
        String idOrder  = id;

        Item item1 = new Item("123456A", 2, 35);
        Item item2 = new Item("123456B", 1, 15);

        List<Item> items = new ArrayList<>();

        items.add(item1);
        items.add(item2);

        return new Order(idOrder, numberOrder, items, status.getValue());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
