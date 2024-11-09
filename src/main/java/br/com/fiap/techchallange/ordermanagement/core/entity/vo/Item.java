package br.com.fiap.techchallange.ordermanagement.core.entity.vo;

import java.io.Serializable;

public class Item implements Serializable {

    String sku;
    Integer quantity;
    float unitValue;
    float amount;

    public Item( String sku, Integer quantity, float unitValue) {
        this.sku = sku;
        this.unitValue = unitValue;
        this.setQuantity(quantity);
        this.setAmount();
    }

    public String getSku() {
        return sku;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public float getUnitValue(){
        return this.unitValue;
    }

    private void setQuantity(Integer quantity) {
        if(quantity <= 0){
            throw new IllegalArgumentException(
                    "quantity of item cannot be less than or equal to zero"
            );
        }
            this.quantity = quantity;
    }

    public float getAmount() {
        return amount;
    }

    private void setAmount() {
        this.amount = this.unitValue * this.quantity ;
    }
}
