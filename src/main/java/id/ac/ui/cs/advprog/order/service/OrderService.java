package id.ac.ui.cs.advprog.order.service;

import id.ac.ui.cs.advprog.order.dto.OrderRequestDTO;
import id.ac.ui.cs.advprog.order.dto.UpdateOrderRequestDTO;
import id.ac.ui.cs.advprog.order.dto.OrderResponseDTO;
import id.ac.ui.cs.advprog.order.dto.OrderListResponseDTO;
import id.ac.ui.cs.advprog.order.dto.ResponseDTO;

import java.util.UUID;

public interface OrderService {
    OrderResponseDTO createOrder(OrderRequestDTO orderRequest);

    OrderResponseDTO getOrderById(UUID orderId);
    OrderListResponseDTO getOrdersByCustomerId(UUID customerId);

    OrderResponseDTO updateOrder(UUID orderId, UpdateOrderRequestDTO updateOrderRequest);

    ResponseDTO cancelOrder(UUID orderId);
}