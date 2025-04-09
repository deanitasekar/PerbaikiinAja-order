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
        if (!"PENDING".equals(this.status)) {
            throw new IllegalStateException("Teknisi tidak dapat diubah");
        }
        this.technician = newTechnician;
    }

    public void setStatus(String newStatus) {
        this.status = newStatus;
    }

    public void cancel() {
        if (!"PENDING".equals(this.status)) {
            throw new IllegalStateException("Order tidak dapat dibatalkan");
        }
        this.status = "CANCELLED";
    }
}
