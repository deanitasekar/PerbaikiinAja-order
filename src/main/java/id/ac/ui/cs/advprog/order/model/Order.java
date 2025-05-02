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
    }

    public void setStatus(String status) {
    }

    public void setPaymentMethod(String paymentMethod) {
    }

    public OrderStatus getOrderStatus() {
    }

    public PaymentMethod getPaymentMethodEnum() {
    }

    public boolean canBeUpdated() {
    }

    public boolean canBeCancelled() {
    }

    public void updateTechnician(String technicianId) {
    }

    public void cancel() {
    }

    public void approve() {
    }

    public void reject() {
    }

    public void startProgress() {
    }

    public void complete() {
    }

    public void setRepairEstimate(String repairEstimate) {
    }

    public void setRepairPrice(Double repairPrice) {
    }

    public void setRepairReport(String repairReport) {
    }
}