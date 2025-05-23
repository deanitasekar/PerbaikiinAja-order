package id.ac.ui.cs.advprog.order.enums;

import lombok.Getter;

@Getter
public enum CouponType {
    FIXED("FIXED"),
    PERCENTAGE("PERCENTAGE"),
    RANDOM("RANDOM");

    private final String value;

    private CouponType(String value) {
        this.value = value;
    }

    public static boolean contains(String param) {
        for (CouponType type : CouponType.values()) {
            if (type.name().equals(param)) {
                return true;
            }
        }
        return false;
    }
}
