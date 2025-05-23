package id.ac.ui.cs.advprog.order.controller;

import id.ac.ui.cs.advprog.order.dto.OrderRequestDTO;
import id.ac.ui.cs.advprog.order.dto.UpdateOrderRequestDTO;
import id.ac.ui.cs.advprog.order.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/orders")
@PreAuthorize("hasRole('USER')")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
    }

    private UUID extractCustomerId(Authentication authentication) {
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<Map<String, Object>>> createOrder(
            @RequestBody OrderRequestDTO orderRequest,
            Authentication authentication) {
    }

    @GetMapping
    public CompletableFuture<ResponseEntity<Map<String, Object>>> getOrderHistory(
            Authentication authentication) {
    }

    @GetMapping("/{orderId}")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> getOrderDetail(
            @PathVariable UUID orderId,
            Authentication authentication) {
    }

    @PutMapping("/{orderId}")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> updateOrder(
            @PathVariable UUID orderId,
            @RequestBody UpdateOrderRequestDTO updateRequest,
            Authentication authentication) {
    }

    @DeleteMapping("/{orderId}")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> cancelOrder(
            @PathVariable UUID orderId,
            Authentication authentication) {
    }


    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalState(IllegalStateException ex) {
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(EntityNotFoundException ex) {
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String,Object>> handleAll(Exception ex) {
    }
}
