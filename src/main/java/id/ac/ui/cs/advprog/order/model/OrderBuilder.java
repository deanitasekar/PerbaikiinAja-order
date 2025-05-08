package id.ac.ui.cs.advprog.order.model;

import lombok.Getter;

import java.util.Date;
import java.util.UUID;

@Getter
public class OrderBuilder {
    private UUID customerId;
    private String itemName;
    private String itemCondition;
    private String repairDetails;
    private Date serviceDate;
    private UUID paymentMethodId;
    private UUID couponId;

    public OrderBuilder setCustomerId(UUID customerId) {
        this.customerId = customerId;
        return this;
    }

    public OrderBuilder setItemName(String itemName) {
        this.itemName = itemName;
        return this;
    }

    public OrderBuilder setItemCondition(String itemCondition) {
        this.itemCondition = itemCondition;
        return this;
    }

    public OrderBuilder setRepairDetails(String repairDetails) {
        this.repairDetails = repairDetails;
        return this;
    }

    public OrderBuilder setServiceDate(Date serviceDate) {
        this.serviceDate = serviceDate;
        return this;
    }

    public OrderBuilder setPaymentMethodId(UUID paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
        return this;
    }

    public OrderBuilder setCouponId(UUID couponId) {
        this.couponId = couponId;
        return this;
    }

    public Order build() {
        return new Order(this);
    }
}