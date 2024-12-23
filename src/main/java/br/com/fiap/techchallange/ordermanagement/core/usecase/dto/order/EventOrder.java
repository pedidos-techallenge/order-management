package br.com.fiap.techchallange.ordermanagement.core.usecase.dto.order;

public record EventOrder(int number_order, String process, String idOrder) {

    public EventOrder(int number_order, String process) {
        this(number_order, process, null);
    }

    public enum TypeEventOrder {
        PAYMENTAPPROVE("paymentApprove"), PAYMENTDENIED("paymentDenied"), PREPARATIONFOOD("preparationFood"), FOODDONE("foodDone"), DELIVERYFOOD("deliveryFood");

        private final String value;

        TypeEventOrder(String typeEventOrder) {
            value = typeEventOrder;
        }

        public String getValue() {
            return value;
        }

        public static TypeEventOrder fromValue(String value) {
            for (TypeEventOrder type : TypeEventOrder.values()) {
                if (type.getValue().equals(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Valor inválido: " + value);
        }
    }
}


