package id.ac.ui.cs.advprog.order.model;

import id.ac.ui.cs.advprog.order.enums.OrderStatus;
import id.ac.ui.cs.advprog.order.enums.PaymentMethod;
import lombok.Getter;

import java.util.Date;

@Getter
public class OrderBuilder {
    private String customerId;
    private String itemName;
    private String itemCondition;
    private String repairRequest;
    private Date serviceDate;
    private String paymentMethod;

    private String status;
    private String technicianId;
    private boolean usingCoupon;
    private String couponCode;
    private String paymentDetails;

    public OrderBuilder(String customerId, String itemName, String itemCondition,
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

    public OrderBuilder setStatus(String status) {
        if (OrderStatus.contains(status)) {
            this.status = status;
        } else {
            throw new IllegalArgumentException("Invalid order status: " + status);
        }
        return this;
    }

    public OrderBuilder setTechnicianId(String technicianId) {
        if (technicianId != null && !technicianId.isEmpty()) {
            this.technicianId = technicianId;
            this.status = OrderStatus.WAITING_APPROVAL.name();
        }
        return this;
    }

    public OrderBuilder setRandomTechnician(String[] availableTechnicianIds) {
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