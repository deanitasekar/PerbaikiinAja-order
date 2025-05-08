package id.ac.ui.cs.advprog.order.model;

import lombok.Getter;

import java.util.Date;
import java.util.UUID;
@Getter
public class OrderBuilder {
    private UUID customerId;
    private String itemName;
    private String itemCondition;
    private String issueDescription;
    private Date desiredServiceDate;

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

    public OrderBuilder setIssueDescription(String issueDescription) {
        this.issueDescription = issueDescription;
        return this;
    }

    public OrderBuilder setDesiredServiceDate(Date desiredServiceDate) {
        this.desiredServiceDate = desiredServiceDate;
        return this;
    }

    public Order build() {
        return new Order(this);
    }
}