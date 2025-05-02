package id.ac.ui.cs.advprog.order.controller;

import id.ac.ui.cs.advprog.order.model.Order;
import id.ac.ui.cs.advprog.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        Order createdOrder = orderService.createOrder(order);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable UUID orderId) {
        Order order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Order>> getOrdersByCustomerId(@PathVariable UUID customerId) {
        List<Order> orders = orderService.getOrdersByCustomerId(customerId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/technician/{technicianId}")
    public ResponseEntity<List<Order>> getOrdersByTechnicianId(@PathVariable UUID technicianId) {
        List<Order> orders = orderService.getOrdersByTechnicianId(technicianId);
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<Order> updateOrder(
            @PathVariable UUID orderId,
            @RequestBody Order order) {
        Order updatedOrder = orderService.updateOrder(orderId, order);
        return ResponseEntity.ok(updatedOrder);
    }

    @PutMapping("/{orderId}/technician/{technicianId}")
    public ResponseEntity<Order> assignTechnician(
            @PathVariable UUID orderId,
            @PathVariable UUID technicianId) {
        Order updatedOrder = orderService.assignTechnician(orderId, technicianId);
        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Order> cancelOrder(@PathVariable UUID orderId) {
        Order cancelledOrder = orderService.cancelOrder(orderId);
        return ResponseEntity.ok(cancelledOrder);
    }

    @PutMapping("/{orderId}/estimate")
    public ResponseEntity<Order> provideEstimate(
            @PathVariable UUID orderId,
            @RequestParam String repairEstimate,
            @RequestParam Double repairPrice) {
        Order updatedOrder = orderService.provideEstimate(orderId, repairEstimate, repairPrice);
        return ResponseEntity.ok(updatedOrder);
    }

    @PutMapping("/{orderId}/approve")
    public ResponseEntity<Order> approveOrder(@PathVariable UUID orderId) {
        Order approvedOrder = orderService.approveOrder(orderId);
        return ResponseEntity.ok(approvedOrder);
    }

    @PutMapping("/{orderId}/reject")
    public ResponseEntity<Order> rejectOrder(@PathVariable UUID orderId) {
        Order rejectedOrder = orderService.rejectOrder(orderId);
        return ResponseEntity.ok(rejectedOrder);
    }

    @PutMapping("/{orderId}/start")
    public ResponseEntity<Order> startProgress(@PathVariable UUID orderId) {
        Order startedOrder = orderService.startProgress(orderId);
        return ResponseEntity.ok(startedOrder);
    }

    @PutMapping("/{orderId}/complete")
    public ResponseEntity<Order> completeOrder(@PathVariable UUID orderId) {
        Order completedOrder = orderService.completeOrder(orderId);
        return ResponseEntity.ok(completedOrder);
    }

    @PutMapping("/{orderId}/report")
    public ResponseEntity<Order> submitRepairReport(
            @PathVariable UUID orderId,
            @RequestParam String repairReport) {
        Order updatedOrder = orderService.submitRepairReport(orderId, repairReport);
        return ResponseEntity.ok(updatedOrder);
    }
}