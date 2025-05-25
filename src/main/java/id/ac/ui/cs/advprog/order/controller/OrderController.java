package id.ac.ui.cs.advprog.order.controller;

import id.ac.ui.cs.advprog.order.dto.OrderListResponseDTO;
import id.ac.ui.cs.advprog.order.dto.OrderRequestDTO;
import id.ac.ui.cs.advprog.order.dto.OrderResponseDTO;
import id.ac.ui.cs.advprog.order.dto.UpdateOrderRequestDTO;
import id.ac.ui.cs.advprog.order.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
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

    private static final String FORBIDDEN = "Forbidden";


    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    private UUID extractUserId(Authentication auth) {
        return UUID.fromString(auth.getName());
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public CompletableFuture<ResponseEntity<Map<String, Object>>> createOrder(
            @RequestBody OrderRequestDTO orderRequest,
            Authentication authentication) {

        UUID customerId = extractUserId(authentication);
        orderRequest.setCustomerId(customerId);

        return orderService.createOrder(orderRequest)
                .thenApply(orderResponse -> {
                    Map<String, Object> body = new HashMap<>();
                    body.put("order", orderResponse);
                    body.put("message", "Order created successfully");
                    return ResponseEntity
                            .status(HttpStatus.CREATED)
                            .body(body);
                });
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public CompletableFuture<ResponseEntity<OrderListResponseDTO>> getOrderHistory(
            Authentication auth) {
        UUID customerId = extractUserId(auth);
        return orderService.getOrdersByCustomerId(customerId)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/{orderId}")
    @PreAuthorize("hasRole('USER')")
    public CompletableFuture<ResponseEntity<OrderResponseDTO>> getOrderDetail(
            @PathVariable UUID orderId,
            Authentication auth) {
        UUID customerId = extractUserId(auth);

        return orderService.getOrderById(orderId)
                .thenApply(dto -> {
                    if (!dto.getCustomerId().equals(customerId)) {
                        throw new AccessDeniedException(FORBIDDEN);
                    }
                    return ResponseEntity.ok(dto);
                });
    }

    @PutMapping("/{orderId}")
    @PreAuthorize("hasRole('USER')")
    public CompletableFuture<ResponseEntity<OrderResponseDTO>> updateOrder(
            @PathVariable UUID orderId,
            @RequestBody @Valid UpdateOrderRequestDTO request,
            Authentication auth) {
        UUID customerId = extractUserId(auth);

        return orderService.getOrderById(orderId)
                .thenCompose(dto -> {
                    if (!dto.getCustomerId().equals(customerId)) {
                        throw new AccessDeniedException(FORBIDDEN);
                    }
                    return orderService.updateOrder(orderId, request);
                })
                .thenApply(ResponseEntity::ok);
    }
    @DeleteMapping("/{orderId}")
    @PreAuthorize("hasRole('USER')")
    public CompletableFuture<ResponseEntity<Void>> cancelOrder(
            @PathVariable UUID orderId,
            Authentication auth) {
        UUID customerId = extractUserId(auth);

        return orderService.getOrderById(orderId)
                .thenCompose(dto -> {
                    if (!dto.getCustomerId().equals(customerId)) {
                        throw new AccessDeniedException(FORBIDDEN);
                    }
                    return orderService.cancelOrder(orderId);
                })
                .thenApply(v -> ResponseEntity.noContent().build());
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public CompletableFuture<ResponseEntity<OrderListResponseDTO>> getAllOrdersAdmin(
            Authentication auth) {
        return orderService.getAllOrders()
                .thenApply(ResponseEntity::ok);
    }

    @ControllerAdvice(assignableTypes = OrderController.class)
    public static class GlobalExceptionHandler {

        @ExceptionHandler(AccessDeniedException.class)
        public ResponseEntity<Void> handleForbidden(AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        @ExceptionHandler(EntityNotFoundException.class)
        public ResponseEntity<Void> handleNotFound(EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<Void> handleAll(Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
