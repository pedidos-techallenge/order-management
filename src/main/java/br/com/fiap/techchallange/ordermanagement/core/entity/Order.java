package br.com.fiap.techchallange.ordermanagement.core.entity;

import br.com.fiap.techchallange.ordermanagement.core.entity.enums.StatusOrder;
import br.com.fiap.techchallange.ordermanagement.core.entity.exceptions.ChangeNotAllowedOrderException;
import br.com.fiap.techchallange.ordermanagement.core.entity.vo.Item;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Order implements Serializable {

    String id;
    Integer numberOrder;
    List<Item> items;
    String status;
    float amount;
    Map<String, Integer> sequenceStatus;

    public Order(String id){
        this.id = id;
        this.numberOrder = 0;
        this.items = new ArrayList<Item>();
        this.status = StatusOrder.OPEN.getValue();
        this.amount  = 0;
        this.sequenceStatus = new HashMap<>();
        loadSequenceStatus();
    }

    public Order(String id, List<Item> items){
        this.id = id;
        this.setItems(items);
        this.numberOrder = 0;
        this.status = StatusOrder.OPEN.getValue();
        this.calculateAmount();
        this.sequenceStatus = new HashMap<>();
        loadSequenceStatus();
    }

    public Order(String id,
                 Integer numberOrder,
                 List<Item> items,
                 String status){
        this.id = id;
        this.numberOrder = numberOrder;
        this.setItems(items);
        this.status = status;
        this.calculateAmount();
        this.sequenceStatus = new HashMap<>();
        loadSequenceStatus();
    }

    public Order(String id, int numberOrder, String status) {
        this.id = id;
        this.numberOrder = numberOrder;
        this.status = status;
        this.sequenceStatus = new HashMap<>();
        loadSequenceStatus();
    }

    private void loadSequenceStatus(){
        sequenceStatus.put(StatusOrder.OPEN.getValue(), 1);
        sequenceStatus.put(StatusOrder.RECEIVED.getValue(), 2);
        sequenceStatus.put(StatusOrder.INPREPARATION.getValue(), 3);
        sequenceStatus.put(StatusOrder.FOODDONE.getValue(), 4);
        sequenceStatus.put(StatusOrder.FINISHED.getValue(), 5);
    }

    private void calculateAmount(){
        this.amount = 0;
        for (Item item :items) {
            this.amount += item.getAmount();
        }
    }

    public void updateStatus(StatusOrder statusOrder) {

        if(statusOrder.equals(StatusOrder.OPEN) || (statusOrder.equals(StatusOrder.RECEIVED))) {
            throw new ChangeNotAllowedOrderException("Alteração de status não permitido");
        }

        if(isSequenceViolation(this.status, statusOrder.getValue())){
            throw new ChangeNotAllowedOrderException("Sequencia do status do pedido violado");
        }

        this.status = statusOrder.getValue();
    }

    private boolean isSequenceViolation(String oldStatus, String newStatus){
        int distance = sequenceStatus.get(newStatus) - sequenceStatus.get(oldStatus);

        return distance != 1;
    }

    public void setNumberOrder(Integer numberOrder){
        if (status.equals(StatusOrder.OPEN.getValue()) && this.numberOrder == 0) {
            this.numberOrder = numberOrder;
        }
    }

    public String getId() {
        return id;
    }

    public float getAmount() {
        return this.amount;
    }

    public String getStatus() {
        return status;
    }

    private void setItems(List<Item> items){
        this.items = items;
        this.calculateAmount();
    }

    public Integer getNumberOrder() {
        return this.numberOrder;
    }

    public List<Item> getItems(){
        List<Item> copyItems = new ArrayList<>();

        for (Item item : items) {
            copyItems.add(new Item(id, item.getSku(), item.getQuantity(), item.getUnitValue()));
        }

        return copyItems;
    }

    private Item copyItem(Item original) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(original);
            oos.flush();
            oos.close();
            bos.close();

            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bis);
            return (Item) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("Not found class payment for orderId "  + this.id);
        }
    }


}
