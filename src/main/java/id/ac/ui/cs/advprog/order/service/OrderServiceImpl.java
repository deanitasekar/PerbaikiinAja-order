package id.ac.ui.cs.advprog.order.service;

import id.ac.ui.cs.advprog.order.dto.OrderListResponseDTO;
import id.ac.ui.cs.advprog.order.dto.OrderRequestDTO;
import id.ac.ui.cs.advprog.order.dto.OrderResponseDTO;
import id.ac.ui.cs.advprog.order.dto.ResponseDTO;
import id.ac.ui.cs.advprog.order.dto.UpdateOrderRequestDTO;
import id.ac.ui.cs.advprog.order.model.Order;
import id.ac.ui.cs.advprog.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
    }

    @Override
    @Async("asyncExecutor")
    public CompletableFuture<OrderResponseDTO> createOrder(OrderRequestDTO orderRequest) {
    }

    @Override
    @Async("asyncExecutor")
    public CompletableFuture<OrderResponseDTO> getOrderById(UUID orderId) {
    }

    @Override
    @Async("asyncExecutor")
    public CompletableFuture<OrderListResponseDTO> getOrdersByCustomerId(UUID customerId) {
    }

    @Override
    @Async("asyncExecutor")
    public CompletableFuture<List<OrderResponseDTO>> getAll() {
    }

    @Override
    @Async("asyncExecutor")
    public CompletableFuture<OrderResponseDTO> updateOrder(UUID orderId, UpdateOrderRequestDTO upd) {
    }

    @Override
    @Async("asyncExecutor")
    public CompletableFuture<ResponseDTO> cancelOrder(UUID orderId) {
    }

    private OrderResponseDTO convertToDto(Order order) {
    }

    private void updateFields(Order order, UpdateOrderRequestDTO update) {
    }
}