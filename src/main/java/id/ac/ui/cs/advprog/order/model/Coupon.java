package id.ac.ui.cs.advprog.order.model;

import id.ac.ui.cs.advprog.order.enums.CouponType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Coupon {

    private String code;
    private CouponType couponType;
    private double value;
    private int maxUsage;
    private int usedCount;

    public Coupon(CouponType couponType, double value, int maxUsage, int id) {
        // constructor body
    }

    public boolean isValid() {
        // method body
        return false;
    }

    public void incrementUsage() {
        // method body
    }
}
