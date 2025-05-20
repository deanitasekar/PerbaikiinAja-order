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
    private double discount_amount;
    private CouponType coupon_type;
    private int max_usage;
    private int current_usage;
    private LocalDateTime start_date;
    private LocalDateTime end_date;
    private LocalDateTime deleted_at;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private UUID created_by;

    public Coupon(CouponType coupon_type, double discount_amount, int max_usage) {
        this.id = UUID.randomUUID();
        this.coupon_type = coupon_type;
        this.discount_amount = discount_amount;
        this.max_usage = max_usage;
        this.current_usage = 0;

        String suffix = id.toString().replace("-", "").substring(0, 3).toUpperCase();
        this.code = coupon_type.name() + "-" + suffix;

        this.created_at = LocalDateTime.now();
        this.start_date = LocalDateTime.now();
    }

    public boolean isValid() {
        LocalDateTime now = LocalDateTime.now();
        if (discount_amount < 0) return false;
        if (current_usage >= max_usage) return false;
        if (deleted_at != null) return false;
        if (start_date != null && now.isBefore(start_date)) return false;
        if (end_date != null && now.isAfter(end_date)) return false;
        return true;
    }

    public void incrementUsage() {
        if (current_usage < max_usage) {
            current_usage++;
        }
    }
}
