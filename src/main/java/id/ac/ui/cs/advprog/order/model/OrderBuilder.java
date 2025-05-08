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
    }

    public OrderBuilder setItemName(String itemName) {
    }

    public OrderBuilder setItemCondition(String itemCondition) {
    }

    public OrderBuilder setIssueDescription(String issueDescription) {
    }

    public OrderBuilder setDesiredServiceDate(Date desiredServiceDate) {
    }

    public Order build() {
    }
}