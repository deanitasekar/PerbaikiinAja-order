package id.ac.ui.cs.advprog.order.controller;

import id.ac.ui.cs.advprog.order.model.Order;
import id.ac.ui.cs.advprog.order.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    public ResponseEntity<Order> createOrder(Order order) {
        return null;
    }

    // Get all orders - Admin only endpoint
    public ResponseEntity<List<Order>> getAllOrders() {
        return null;
    }

    // Get order by ID
    public ResponseEntity<Order> getOrderById(UUID orderId) {
        return null;
    }

    public ResponseEntity<List<Order>> getOrdersByCustomerId(UUID customerId) {
        return null;
    }

    public ResponseEntity<List<Order>> getOrdersByTechnicianId(UUID technicianId) {
        return null;
    }

    public ResponseEntity<Order> updateOrder(UUID orderId, Order order) {
        return null;
    }

    public ResponseEntity<Order> assignTechnician(UUID orderId, UUID technicianId) {
        return null;
    }

    public ResponseEntity<Order> cancelOrder(UUID orderId) {
        return null;
    }

    public ResponseEntity<Order> provideEstimate(UUID orderId, String repairEstimate, Double repairPrice) {
        return null;
    }

    public ResponseEntity<Order> approveOrder(UUID orderId) {
        return null;
    }

    public ResponseEntity<Order> rejectOrder(UUID orderId) {
        return null;
    }

    public ResponseEntity<Order> startProgress(UUID orderId) {
        return null;
    }

    public ResponseEntity<Order> completeOrder(UUID orderId) {
        return null;
    }

    public ResponseEntity<Order> submitRepairReport(UUID orderId, String repairReport) {
        return null;
    }
}