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

    public Coupon(CouponType couponType, double discountAmount, int maxUsage) {
        this.id = UUID.randomUUID();
        this.couponType = couponType;
        this.discountAmount = discountAmount;
        this.maxUsage = maxUsage;
        this.currentUsage = 0;

        String suffix = id.toString().replace("-", "").substring(0, 3).toUpperCase();
        this.code = couponType.name() + "-" + suffix;

        this.createdAt = LocalDateTime.now();
        this.startDate = this.startDate != null ? this.startDate : this.createdAt;
    }

    public boolean isValid() {
        LocalDateTime now = LocalDateTime.now();
        if (discountAmount < 0) return false;
        if (currentUsage >= maxUsage) return false;
        if (deletedAt != null) return false;
        if (startDate != null && now.isBefore(startDate)) return false;
        if (endDate != null && now.isAfter(endDate)) return false;
        return true;
    }

    public void incrementUsage() {
        if (currentUsage < maxUsage) {
            currentUsage++;
        }
    }
}
