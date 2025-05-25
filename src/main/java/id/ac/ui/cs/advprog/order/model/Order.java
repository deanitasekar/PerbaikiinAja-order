package id.ac.ui.cs.advprog.order.model;

import id.ac.ui.cs.advprog.order.builder.OrderBuilder;
import id.ac.ui.cs.advprog.order.enums.OrderStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "customer_id", nullable = false, updatable = false)
    private UUID customerId;

    @Column(name = "technician_id")
    private UUID technicianId;

    @Column(name = "item_name", nullable = false)
    private String itemName;

    @Column(name = "item_condition", nullable = false, columnDefinition = "TEXT")
    private String itemCondition;

    @Column(name = "repair_details", nullable = false, columnDefinition = "TEXT")
    private String repairDetails;

    @Column(name = "service_date", nullable = false)
    private Date serviceDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @Column(name = "payment_method_id")
    private UUID paymentMethodId;

    @Column(name = "coupon_id")
    private UUID couponId;

    @Column(name = "estimated_completion_time")
    private String estimatedCompletionTime;

    @Column(name = "estimated_price", precision = 19, scale = 2)
    private BigDecimal estimatedPrice;

    @Column(name = "final_price", precision = 19, scale = 2)
    private BigDecimal finalPrice;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    public Order(OrderBuilder builder) {
        this.customerId = builder.getCustomerId();
        this.technicianId = builder.getTechnicianId();
        this.itemName = builder.getItemName();
        this.itemCondition = builder.getItemCondition();
        this.repairDetails = builder.getRepairDetails();
        this.serviceDate = builder.getServiceDate();
        this.paymentMethodId = builder.getPaymentMethodId();
        this.couponId = builder.getCouponId();
        this.status = OrderStatus.PENDING;
    }
}