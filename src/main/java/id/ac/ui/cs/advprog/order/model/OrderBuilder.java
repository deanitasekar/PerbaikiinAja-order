package id.ac.ui.cs.advprog.order.model;

public class OrderBuilder {

    private String orderId;
    private String itemName;
    private String itemCondition;

    public OrderBuilder withOrderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    public OrderBuilder withItemName(String itemName) {
        this.itemName = itemName;
        return this;
    }

    public OrderBuilder withItemCondition(String itemCondition) {
        this.itemCondition = itemCondition;
        return this;
    }

    public Order build() {
        return new Order(orderId, itemName, itemCondition);
    }
}