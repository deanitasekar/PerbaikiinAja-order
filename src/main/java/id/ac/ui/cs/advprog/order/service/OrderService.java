package id.ac.ui.cs.advprog.order.service;

import id.ac.ui.cs.advprog.order.model.Order;
import java.util.List;
import java.util.UUID;

public interface OrderService {
    Order createOrder(Order order);
    List<Order> getCustomerOrders(UUID customerId);
    Order findById(UUID orderId);
    Order updateStatus(UUID orderId, String newStatus);
    Order updateOrderDetails(UUID orderId, Order updatedOrder) throws Exception;
    Order changeTechnician(UUID orderId, UUID newTechnicianId) throws Exception;
    void cancelOrder(UUID orderId, UUID userId) throws Exception;
    Order completeOrder(UUID orderId, UUID technicianId, String repairReport) throws Exception;
}