package id.ac.ui.cs.advprog.order.model;
import id.ac.ui.cs.advprog.order.enums.OrderStatus;
import id.ac.ui.cs.advprog.order.enums.PaymentMethod;

public class Order {

    private String orderId;
    private String itemName;
    private String itemCondition;
    private String technician;

    private OrderStatus status;
    private PaymentMethod paymentMethod;

    public Order(String orderId, String itemName, String itemCondition,
                 String statusStr, String paymentMethodStr) {
        this.orderId = orderId;
        this.itemName = itemName;
        this.itemCondition = itemCondition;

        if (!OrderStatus.contains(statusStr)) {
            throw new IllegalArgumentException("Invalid OrderStatus");
        }
        this.status = OrderStatus.valueOf(statusStr);

        if (!PaymentMethod.contains(paymentMethodStr)) {
            throw new IllegalArgumentException("Invalid PaymentMethod");
        }
        this.paymentMethod = PaymentMethod.valueOf(paymentMethodStr);
    }

    public String getOrderId() {
        return orderId;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemCondition() {
        return itemCondition;
    }

    public String getTechnician() {
        return technician;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }


    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemCondition(String itemCondition) {
        this.itemCondition = itemCondition;
    }

    public void setTechnician(String newTechnician) {
        if (this.status != OrderStatus.PENDING) {
            throw new IllegalStateException("Teknisi tidak dapat diubah");
        }
        this.technician = newTechnician;
    }

    public void setStatus(String newStatusStr) {
        if (!OrderStatus.contains(newStatusStr)) {
            throw new IllegalArgumentException("Invalid status");
        }
        this.status = OrderStatus.valueOf(newStatusStr);
    }


    public void setStatus(OrderStatus newStatus) {
        this.status = newStatus;
    }

    public void setPaymentMethod(String paymentMethodStr) {
        if (!PaymentMethod.contains(paymentMethodStr)) {
            throw new IllegalArgumentException("Invalid payment method");
        }
        this.paymentMethod = PaymentMethod.valueOf(paymentMethodStr);
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void cancel() {
        if (this.status != OrderStatus.PENDING) {
            throw new IllegalStateException("Order tidak dapat dibatalkan");
        }
        this.status = OrderStatus.CANCELLED;
    }
}
