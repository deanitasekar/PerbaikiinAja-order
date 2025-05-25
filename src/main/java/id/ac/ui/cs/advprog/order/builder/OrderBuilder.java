package id.ac.ui.cs.advprog.order.builder;

import id.ac.ui.cs.advprog.order.model.Order;

import java.util.Date;
import java.util.UUID;

public class OrderBuilder {
    private UUID customerId;
    private UUID technicianId;
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

    public OrderBuilder setTechnicianId(UUID technicianId) {
        this.technicianId = technicianId;
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

    public UUID getCustomerId() { return customerId; }
    public UUID getTechnicianId() { return technicianId; }
    public String getItemName() { return itemName; }
    public String getItemCondition() { return itemCondition; }
    public String getRepairDetails() { return repairDetails; }
    public Date getServiceDate() { return serviceDate; }
    public UUID getPaymentMethodId() { return paymentMethodId; }
    public UUID getCouponId() { return couponId; }
}
