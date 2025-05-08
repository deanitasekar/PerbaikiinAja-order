package id.ac.ui.cs.advprog.order.controller;

import id.ac.ui.cs.advprog.order.dto.OrderListResponseDTO;
import id.ac.ui.cs.advprog.order.dto.OrderRequestDTO;
import id.ac.ui.cs.advprog.order.dto.OrderResponseDTO;
import id.ac.ui.cs.advprog.order.dto.ResponseDTO;
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

import java.util.UUID;
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
    public ResponseEntity<OrderResponseDTO> createOrder(
            @RequestBody OrderRequestDTO orderRequest,
            Authentication authentication) {
        UUID customerId = extractCustomerId(authentication);
        orderRequest.setCustomerId(customerId);

        OrderResponseDTO response = orderService.createOrder(orderRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<OrderListResponseDTO> getOrderHistory(
            Authentication authentication) {
        UUID customerId = extractCustomerId(authentication);
        OrderListResponseDTO response = orderService.getOrdersByCustomerId(customerId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderResponseDTO> updateOrder(
            @PathVariable UUID orderId,
            @RequestBody UpdateOrderRequestDTO updateRequest,
            Authentication authentication) {
        UUID customerId = extractCustomerId(authentication);

        OrderResponseDTO existingOrder = orderService.getOrderById(orderId);
        if (!existingOrder.getCustomerId().equals(customerId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        OrderResponseDTO response = orderService.updateOrder(orderId, updateRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<ResponseDTO> cancelOrder(
            @PathVariable UUID orderId,
            Authentication authentication) {
        try {
            UUID customerId = extractCustomerId(authentication);

            OrderResponseDTO existingOrder = orderService.getOrderById(orderId);

            if (!existingOrder.getCustomerId().equals(customerId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            ResponseDTO response = orderService.cancelOrder(orderId);
            return ResponseEntity.ok(response);

        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseDTO.builder()
                            .success(false)
                            .message(ex.getMessage())
                            .build());
        } catch (IllegalStateException ex) {
            return ResponseEntity.badRequest()
                    .body(ResponseDTO.builder()
                            .success(false)
                            .message(ex.getMessage())
                            .build());
        }
    }


    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ResponseDTO> handleOrderExceptions(Exception ex) {
        return ResponseEntity.badRequest().body(
                ResponseDTO.builder()
                        .success(false)
                        .message(ex.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ResponseDTO> handleEntityNotFoundException(EntityNotFoundException ex) {
        ResponseDTO responseDTO = ResponseDTO.builder()
                .success(false)
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
    }
}