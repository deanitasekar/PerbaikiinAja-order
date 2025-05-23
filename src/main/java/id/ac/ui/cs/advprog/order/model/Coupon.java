package id.ac.ui.cs.advprog.order.model;

import id.ac.ui.cs.advprog.order.enums.CouponType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "Coupon")
@Getter
@Setter
@NoArgsConstructor

public class Coupon {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name="UUID", strategy="org.hibernate.id.UUIDGenerator")
    @Column(name="id", updatable=false, nullable=false)
    private UUID id;
    @Column(unique=true)
    private String code;
    private double discount_amount;
    private CouponType couponType;
    private int max_usage;
    private int current_usage;
    private LocalDateTime start_date;
    private LocalDateTime end_date;
    private LocalDateTime deleted_at;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
//    private UUID created_by;
    public Coupon(CouponType couponType, double discount_amount, int max_usage) {
        this.couponType = couponType;
        this.discount_amount = discount_amount;
        this.max_usage = max_usage;
        this.current_usage = 0;
        this.created_at = LocalDateTime.now();
        this.start_date = LocalDateTime.now();
    }
    @PrePersist
    public void prePersist() {
        if (created_at == null) created_at = LocalDateTime.now();
        if (start_date == null) start_date = LocalDateTime.now();
        if (code == null && id != null) {
            String suffix = id.toString().replace("-", "").substring(0,3).toUpperCase();
            code = couponType.name() + "-" + suffix;
        }
    }

    @PreUpdate
    public void preUpdate() {
        updated_at = LocalDateTime.now();
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
        if (current_usage < max_usage) current_usage++;
    }
}