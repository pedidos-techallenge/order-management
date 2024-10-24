package br.com.fiap.techchallange.ordermanagement.core.entity.enums;

public enum StatusPayment {
   PAID("PAID"), DENIED("DENIED");

    private final String value;

    StatusPayment(String valueStatus){
        value = valueStatus;
    }

    public String getValue(){
        return value;
    }

    public static StatusPayment fromValue(String value) {
        for (StatusPayment status : StatusPayment.values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Valor inválido: " + value);
    }
}
