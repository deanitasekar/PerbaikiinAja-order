package id.ac.ui.cs.advprog.order.model;

import id.ac.ui.cs.advprog.order.enums.CouponType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class Coupon {

    private UUID id;
    private String code;
    private double discountAmount;
    private CouponType couponType;
    private int maxUsage;
    private int currentUsage;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime deletedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID createdBy;

    public Coupon(CouponType discountType, double discountAmount, int maxUsage, int index) {
        // constructor body
    }

    public boolean isValid() {
        // validation logic
        return false;
    }

    public void incrementUsage() {
        // increment logic
    }
}
