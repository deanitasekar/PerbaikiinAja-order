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
    }

    public OrderBuilder setStatus(OrderStatus status) {
    }

    public OrderBuilder setStatus(String status) {
    }

    public OrderBuilder setTechnicianId(String technicianId) {
    }

    public OrderBuilder setRandomTechnician(String[] availableTechnicianIds) {
    }

    public OrderBuilder setPaymentMethod(PaymentMethod paymentMethod) {
    }

    public OrderBuilder setCustomPaymentDetails(String paymentDetails) {
    }

    public OrderBuilder setUsingCoupon(boolean usingCoupon) {
    }

    public OrderBuilder setCouponCode(String couponCode) {
    }

    public Order build() {
    }
}