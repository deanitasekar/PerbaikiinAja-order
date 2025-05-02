package id.ac.ui.cs.advprog.order.model;

import id.ac.ui.cs.advprog.order.enums.OrderStatus;
import id.ac.ui.cs.advprog.order.enums.PaymentMethod;
import lombok.Getter;

import java.util.Date;
import java.util.UUID;

@Getter
public class OrderBuilder {
    private UUID customerId;
    private String itemName;
    private String itemCondition;
    private String repairRequest;
    private Date serviceDate;
    private String paymentMethod;

    private String status;
    private UUID technicianId;
    private boolean usingCoupon;
    private String couponCode;
    private String paymentDetails;

    public OrderBuilder(UUID customerId, String itemName, String itemCondition,
                        String repairRequest, Date serviceDate, PaymentMethod paymentMethod) {
        this.customerId = customerId;
        this.itemName = itemName;
        this.itemCondition = itemCondition;
        this.repairRequest = repairRequest;
        this.serviceDate = serviceDate;
        this.paymentMethod = paymentMethod.getValue();
        this.status = OrderStatus.PENDING.name();
        this.usingCoupon = false;
    }

    public OrderBuilder setStatus(OrderStatus status) {
        this.status = status.name();
        return this;
    }

    public OrderBuilder setTechnicianId(UUID technicianId) {
        if (technicianId != null) {
            this.technicianId = technicianId;
            this.status = OrderStatus.WAITING_APPROVAL.name();
        }
        return this;
    }

    public OrderBuilder setRandomTechnician(UUID[] availableTechnicianIds) {
        if (availableTechnicianIds != null && availableTechnicianIds.length > 0) {
            java.util.Random random = new java.util.Random();
            int randomIndex = random.nextInt(availableTechnicianIds.length);
            this.technicianId = availableTechnicianIds[randomIndex];
            this.status = OrderStatus.WAITING_APPROVAL.name();
        }
        return this;
    }

    public OrderBuilder setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod.getValue();
        return this;
    }

    public OrderBuilder setCustomPaymentDetails(String paymentDetails) {
        this.paymentDetails = paymentDetails;
        return this;
    }

    public OrderBuilder setUsingCoupon(boolean usingCoupon) {
        this.usingCoupon = usingCoupon;
        return this;
    }

    public OrderBuilder setCouponCode(String couponCode) {
        this.couponCode = couponCode;
        if (couponCode != null && !couponCode.isEmpty()) {
            this.usingCoupon = true;
        } else {
            this.usingCoupon = false;
        }
        return this;
    }

    public Order build() {
        if (paymentDetails == null) {
            this.paymentDetails = this.paymentMethod;
        }

        return new Order(this);
    }
}