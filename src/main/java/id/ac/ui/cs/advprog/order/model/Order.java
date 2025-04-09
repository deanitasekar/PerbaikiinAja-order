package id.ac.ui.cs.advprog.order.model;

public class Order {

    private String orderId;
    private String itemName;
    private String itemCondition;
    private String technician;
    private String status;

    public Order(String orderId, String itemName, String itemCondition) {
        this.orderId = orderId;
        this.itemName = itemName;
        this.itemCondition = itemCondition;
        this.status = "PENDING";
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

    public String getStatus() {
        return status;
    }

    public void setTechnician(String newTechnician) {
    }

    public void setStatus(String newStatus) {
    }

    public void cancel() {
    }
}
