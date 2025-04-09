package id.ac.ui.cs.advprog.order.model;

import id.ac.ui.cs.advprog.order.enums.OrderStatus;
import id.ac.ui.cs.advprog.order.enums.PaymentMethod;

public class OrderBuilder {

    private String orderId;
    private String itemName;
    private String itemCondition;
    private String technician;

    private OrderStatus status = OrderStatus.PENDING;
    private PaymentMethod paymentMethod = PaymentMethod.BANK_TRANSFER;

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

    public OrderBuilder withTechnician(String technician) {
        this.technician = technician;
        return this;
    }

    public OrderBuilder withStatus(OrderStatus status) {
        this.status = status;
        return this;
    }

    public OrderBuilder withPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }


    public Order build() {
        Order order = new Order(
                this.orderId,
                this.itemName,
                this.itemCondition,
                this.status.toString(),
                this.paymentMethod.toString()
        );

        if (this.technician != null) {
            order.setTechnician(this.technician);
        }

        return order;
    }
}
