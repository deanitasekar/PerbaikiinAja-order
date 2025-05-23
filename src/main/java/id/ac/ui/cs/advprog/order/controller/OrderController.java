package id.ac.ui.cs.advprog.order.controller;

import id.ac.ui.cs.advprog.order.dto.OrderRequestDTO;
import id.ac.ui.cs.advprog.order.dto.UpdateOrderRequestDTO;
import id.ac.ui.cs.advprog.order.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
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
        this.orderService = orderService;
    }

    private UUID extractCustomerId(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof String) {
            return UUID.fromString((String) principal);
        } else if (principal instanceof User) {
            return UUID.fromString(((User) principal).getUsername());
        }
        throw new IllegalStateException("Unexpected principal type");
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<Map<String, Object>>> createOrder(
            @RequestBody OrderRequestDTO orderRequest,
            Authentication authentication) {

        UUID customerId = extractCustomerId(authentication);
        orderRequest.setCustomerId(customerId);

        return orderService.createOrder(orderRequest)
                .thenApply(orderResponse -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("order", orderResponse);
                    response.put(MESSAGE_KEY, "Order created successfully");
                    return ResponseEntity.status(HttpStatus.CREATED).body(response);
                });
    }

    @GetMapping
    public CompletableFuture<ResponseEntity<Map<String, Object>>> getOrderHistory(
            Authentication authentication) {

        UUID customerId = extractCustomerId(authentication);
        return orderService.getOrdersByCustomerId(customerId)
                .thenApply(orderList -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("orders", orderList.getOrders());
                    response.put("count", orderList.getCount());
                    response.put(MESSAGE_KEY, "Orders retrieved successfully");
                    return ResponseEntity.ok(response);
                });
    }

    @GetMapping("/{orderId}")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> getOrderDetail(
            @PathVariable UUID orderId,
            Authentication authentication) {

        UUID customerId = extractCustomerId(authentication);
        return orderService.getOrderById(orderId)
                .thenApply(orderResponse -> {
                    if (!orderResponse.getCustomerId().equals(customerId)) {
                        throw new IllegalStateException(FORBIDDEN_MESSAGE);
                    }
                    Map<String, Object> response = new HashMap<>();
                    response.put("order", orderResponse);
                    response.put(MESSAGE_KEY, "Order retrieved successfully");
                    return ResponseEntity.ok(response);
                });
    }

    @PutMapping("/{orderId}")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> updateOrder(
            @PathVariable UUID orderId,
            @RequestBody UpdateOrderRequestDTO updateRequest,
            Authentication authentication) {

        UUID customerId = extractCustomerId(authentication);
        return orderService.getOrderById(orderId)
                .thenCompose(existingOrder -> {
                    if (!existingOrder.getCustomerId().equals(customerId)) {
                        throw new IllegalStateException(FORBIDDEN_MESSAGE);
                    }
                    return orderService.updateOrder(orderId, updateRequest);
                })
                .thenApply(updatedOrder -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("order", updatedOrder);
                    response.put(MESSAGE_KEY,
                            "Order ID " + updatedOrder.getId() + " updated successfully");
                    return ResponseEntity.ok(response);
                });
    }

    @DeleteMapping("/{orderId}")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> cancelOrder(
            @PathVariable UUID orderId,
            Authentication authentication) {

        UUID customerId = extractCustomerId(authentication);
        return orderService.getOrderById(orderId)
                .thenCompose(existingOrder -> {
                    if (!existingOrder.getCustomerId().equals(customerId)) {
                        throw new IllegalStateException(FORBIDDEN_MESSAGE);
                    }
                    return orderService.cancelOrder(orderId);
                })
                .thenApply(cancelResponse -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", cancelResponse.isSuccess());
                    response.put(MESSAGE_KEY, cancelResponse.getMessage());
                    return ResponseEntity.ok(response);
                });
    }

    private static final String MESSAGE_KEY = "message";
    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "Something went wrong with the server";
    private static final String FORBIDDEN_MESSAGE = "You are not authorized to access this resource";

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalState(IllegalStateException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", HttpStatus.FORBIDDEN.value());
        response.put("success", false);
        response.put(MESSAGE_KEY, ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(EntityNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", HttpStatus.NOT_FOUND.value());
        response.put("success", false);
        response.put(MESSAGE_KEY, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String,Object>> handleAll(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("error", ex.getMessage());
        response.put(MESSAGE_KEY, INTERNAL_SERVER_ERROR_MESSAGE);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
