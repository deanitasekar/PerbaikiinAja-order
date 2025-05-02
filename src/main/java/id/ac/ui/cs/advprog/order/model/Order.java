package id.ac.ui.cs.advprog.order.model;

import id.ac.ui.cs.advprog.order.enums.OrderStatus;
import id.ac.ui.cs.advprog.order.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Getter @Setter
@Entity
@Table(name = "orders")
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "customer_id", nullable = false)
    private String customerId;

    @Column(name = "technician_id")
    private String technicianId;

    @Column(name = "item_name", nullable = false)
    private String itemName;

    @Column(name = "item_condition", nullable = false, columnDefinition = "TEXT")
    private String itemCondition;

    @Column(name = "repair_request", nullable = false, columnDefinition = "TEXT")
    private String repairRequest;

    @Column(name = "service_date", nullable = false)
    private Date serviceDate;

    @Column(name = "payment_method", nullable = false)
    private String paymentMethod;

    @Column(name = "payment_details")
    private String paymentDetails;

    @Column(name = "using_coupon")
    private boolean usingCoupon;

    @Column(name = "coupon_code")
    private String couponCode;

    @Column(name = "repair_estimate")
    private String repairEstimate;

    @Column(name = "repair_price")
    private Double repairPrice;

    @Column(name = "repair_report", columnDefinition = "TEXT")
    private String repairReport;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    public Order(OrderBuilder builder) {
        this.id = UUID.randomUUID().toString();
        this.customerId = builder.getCustomerId();
        this.itemName = builder.getItemName();
        this.itemCondition = builder.getItemCondition();
        this.repairRequest = builder.getRepairRequest();
        this.serviceDate = builder.getServiceDate();
        this.paymentMethod = builder.getPaymentMethod();
        this.paymentDetails = builder.getPaymentDetails();
        this.status = builder.getStatus();
        this.technicianId = builder.getTechnicianId();
        this.usingCoupon = builder.isUsingCoupon();
        this.couponCode = builder.getCouponCode();
        this.createdAt = new Date();
        this.updatedAt = this.createdAt;
    }

    public void setStatus(String status) {
        if (OrderStatus.contains(status)) {
            this.status = status;
            this.updatedAt = new Date();
        } else {
            throw new IllegalArgumentException("Invalid order status: " + status);
        }
    }

    public void setPaymentMethod(String paymentMethod) {
        if (PaymentMethod.contains(paymentMethod)) {
            this.paymentMethod = paymentMethod;
            this.updatedAt = new Date();
        } else {
            throw new IllegalArgumentException("Invalid payment method: " + paymentMethod);
        }
    }

    public OrderStatus getOrderStatus() {
        return OrderStatus.valueOf(this.status);
    }

    public PaymentMethod getPaymentMethodEnum() {
        return PaymentMethod.fromValue(this.paymentMethod);
    }

    public boolean canBeUpdated() {
        return this.status.equals(OrderStatus.PENDING.name()) ||
                this.status.equals(OrderStatus.WAITING_APPROVAL.name());
    }

    public boolean canBeCancelled() {
        return this.status.equals(OrderStatus.PENDING.name()) ||
                this.status.equals(OrderStatus.WAITING_APPROVAL.name());
    }

    public void updateTechnician(String technicianId) {
        if (!canBeUpdated()) {
            throw new IllegalStateException("Order cannot be updated in current status: " + this.status);
        }
        this.technicianId = technicianId;
        this.status = OrderStatus.WAITING_APPROVAL.name();
        this.updatedAt = new Date();
    }

    public void cancel() {
        if (!canBeCancelled()) {
            throw new IllegalStateException("Order cannot be cancelled in current status: " + this.status);
        }
        this.status = OrderStatus.CANCELLED.name();
        this.updatedAt = new Date();
    }

    public void approve() {
        if (this.technicianId == null || !this.status.equals(OrderStatus.WAITING_APPROVAL.name())) {
            throw new IllegalStateException("Order cannot be approved in current status: " + this.status);
        }
        this.status = OrderStatus.APPROVED.name();
        this.updatedAt = new Date();
    }

    public void reject() {
        if (this.technicianId == null || !this.status.equals(OrderStatus.WAITING_APPROVAL.name())) {
            throw new IllegalStateException("Order cannot be rejected in current status: " + this.status);
        }
        this.status = OrderStatus.REJECTED.name();
        this.updatedAt = new Date();
    }

    public void startProgress() {
        if (!this.status.equals(OrderStatus.APPROVED.name())) {
            throw new IllegalStateException("Order cannot be started in current status: " + this.status);
        }
        this.status = OrderStatus.IN_PROGRESS.name();
        this.updatedAt = new Date();
    }

    public void complete() {
        if (!this.status.equals(OrderStatus.IN_PROGRESS.name())) {
            throw new IllegalStateException("Order cannot be completed in current status: " + this.status);
        }
        this.status = OrderStatus.COMPLETED.name();
        this.updatedAt = new Date();
    }

    public void setRepairEstimate(String repairEstimate) {
        if (this.technicianId == null || !this.status.equals(OrderStatus.WAITING_APPROVAL.name())) {
            throw new IllegalStateException("Cannot set repair estimate in current status: " + this.status);
        }
        this.repairEstimate = repairEstimate;
        this.updatedAt = new Date();
    }

    public void setRepairPrice(Double repairPrice) {
        if (this.technicianId == null || !this.status.equals(OrderStatus.WAITING_APPROVAL.name())) {
            throw new IllegalStateException("Cannot set repair price in current status: " + this.status);
        }
        this.repairPrice = repairPrice;
        this.updatedAt = new Date();
    }

    public void setRepairReport(String repairReport) {
        if (!this.status.equals(OrderStatus.COMPLETED.name())) {
            throw new IllegalStateException("Cannot set repair report in current status: " + this.status);
        }
        this.repairReport = repairReport;
        this.updatedAt = new Date();
    }
}