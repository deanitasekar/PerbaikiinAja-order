package id.ac.ui.cs.advprog.order.service;

import id.ac.ui.cs.advprog.order.dto.OrderRequestDTO;
import id.ac.ui.cs.advprog.order.dto.UpdateOrderRequestDTO;
import id.ac.ui.cs.advprog.order.dto.OrderResponseDTO;
import id.ac.ui.cs.advprog.order.dto.OrderListResponseDTO;
import id.ac.ui.cs.advprog.order.dto.ResponseDTO;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface OrderService {
    CompletableFuture<OrderResponseDTO> createOrder(OrderRequestDTO orderRequest);
    CompletableFuture<OrderResponseDTO> getOrderById(UUID orderId);
    CompletableFuture<OrderListResponseDTO> getOrdersByCustomerId(UUID customerId);
    CompletableFuture<OrderListResponseDTO> getAllOrders();
    CompletableFuture<OrderResponseDTO> updateOrder(UUID orderId, UpdateOrderRequestDTO updateRequest);
    CompletableFuture<ResponseDTO> cancelOrder(UUID orderId);
}