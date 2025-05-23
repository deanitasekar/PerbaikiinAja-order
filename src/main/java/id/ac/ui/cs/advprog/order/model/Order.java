package id.ac.ui.cs.advprog.order.model;

import id.ac.ui.cs.advprog.order.enums.OrderStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Generated
@Getter @Setter
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name="UUID", strategy="org.hibernate.id.UUIDGenerator")
    @Column(name="id", updatable=false, nullable=false)
    private UUID id;

    @Column(name = "customer_id", nullable = false)
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

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "repair_estimate", columnDefinition = "TEXT")
    private String repairEstimate;

    @Column(name = "repair_price")
    private Double repairPrice;

    @Column(name = "repair_report", columnDefinition = "TEXT")
    private String repairReport;

    @Column(name = "payment_method_id")
    private UUID paymentMethodId;

    @Column(name = "coupon_id")
    private UUID couponId;

    @Column(name = "estimated_completion_time")
    private String estimatedCompletionTime;

    @Column(name = "estimated_price")
    private Double estimatedPrice;

    @Column(name = "final_price")
    private Double finalPrice;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    public Order(OrderBuilder builder) {
        this.customerId = builder.getCustomerId();
        this.itemName = builder.getItemName();
        this.itemCondition = builder.getItemCondition();
        this.repairDetails = builder.getRepairDetails();
        this.serviceDate = builder.getServiceDate();
        this.paymentMethodId = builder.getPaymentMethodId();
        this.couponId = builder.getCouponId();
        this.status = OrderStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }
}