package id.ac.ui.cs.advprog.order.enums;

import lombok.Getter;

@Getter
public enum PaymentMethod {
    BANK_TRANSFER("BANK_TRANSFER"),
    E_WALLET("E_WALLET"),
    CASH_ON_DELIVERY("CASH_ON_DELIVERY");

    private final String value;

    PaymentMethod(String value) {
        this.value = value;
    }

    public static boolean contains(String param) {
        for (PaymentMethod paymentMethod : PaymentMethod.values()) {
            if (paymentMethod.getValue().equals(param)) {
                return true;
            }
        }
        return false;
    }
}

