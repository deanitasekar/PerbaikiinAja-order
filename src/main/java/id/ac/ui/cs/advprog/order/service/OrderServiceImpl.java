package id.ac.ui.cs.advprog.order.service;

import id.ac.ui.cs.advprog.order.dto.OrderListResponseDTO;
import id.ac.ui.cs.advprog.order.dto.OrderRequestDTO;
import id.ac.ui.cs.advprog.order.dto.OrderResponseDTO;
import id.ac.ui.cs.advprog.order.dto.ResponseDTO;
import id.ac.ui.cs.advprog.order.dto.UpdateOrderRequestDTO;
import id.ac.ui.cs.advprog.order.enums.OrderStatus;
import id.ac.ui.cs.advprog.order.model.Order;
import id.ac.ui.cs.advprog.order.model.OrderBuilder;
import id.ac.ui.cs.advprog.order.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    @Async("asyncExecutor")
    public CompletableFuture<OrderResponseDTO> createOrder(OrderRequestDTO orderRequest) {
        Order order = new OrderBuilder()
                .setCustomerId(orderRequest.getCustomerId())
                .setItemName(orderRequest.getItemName())
                .setItemCondition(orderRequest.getItemCondition())
                .setRepairDetails(orderRequest.getRepairDetails())
                .setServiceDate(orderRequest.getServiceDate())
                .setPaymentMethodId(orderRequest.getPaymentMethodId())
                .setCouponId(orderRequest.getCouponId())
                .build();

        if (orderRequest.getTechnicianId() != null) {
            order.setTechnicianId(orderRequest.getTechnicianId());
        }

        Order saved = orderRepository.save(order);
        return CompletableFuture.completedFuture(convertToDto(saved));
    }

    @Override
    @Async("asyncExecutor")
    public CompletableFuture<OrderResponseDTO> getOrderById(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + orderId));
        return CompletableFuture.completedFuture(convertToDto(order));
    }

    @Override
    @Async("asyncExecutor")
    public CompletableFuture<OrderListResponseDTO> getOrdersByCustomerId(UUID customerId) {
        List<Order> orders = orderRepository.findByCustomerId(customerId);
        List<OrderResponseDTO> dtos = orders.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return CompletableFuture.completedFuture(
                OrderListResponseDTO.builder()
                        .orders(dtos)
                        .count(dtos.size())
                        .build()
        );
    }

    @Override
    @Async("asyncExecutor")
    public CompletableFuture<List<OrderResponseDTO>> getAll() {
        List<Order> orders = orderRepository.findAll();
        List<OrderResponseDTO> dtos = orders.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return CompletableFuture.completedFuture(dtos);
    }

    @Override
    @Async("asyncExecutor")
    public CompletableFuture<OrderResponseDTO> updateOrder(UUID orderId, UpdateOrderRequestDTO upd) {
        Order existing = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + orderId));
        updateFields(existing, upd);
        existing.setUpdatedAt(LocalDateTime.now());
        Order saved = orderRepository.save(existing);
        return CompletableFuture.completedFuture(convertToDto(saved));
    }

    @Override
    @Async("asyncExecutor")
    public CompletableFuture<ResponseDTO> cancelOrder(UUID orderId) {
        Order existing = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + orderId));
        existing.setStatus(OrderStatus.CANCELLED);
        existing.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(existing);
        return CompletableFuture.completedFuture(
                ResponseDTO.builder()
                        .success(true)
                        .message("Order has been successfully cancelled")
                        .build()
        );
    }

    private OrderResponseDTO convertToDto(Order order) {
        return OrderResponseDTO.builder()
                .id(order.getId())
                .customerId(order.getCustomerId())
                .technicianId(order.getTechnicianId())
                .itemName(order.getItemName())
                .itemCondition(order.getItemCondition())
                .repairDetails(order.getRepairDetails())
                .serviceDate(order.getServiceDate())
                .status(order.getStatus())
                .paymentMethodId(order.getPaymentMethodId())
                .couponId(order.getCouponId())
                .repairEstimate(order.getRepairEstimate())
                .repairPrice(order.getRepairPrice())
                .repairReport(order.getRepairReport())
                .estimatedCompletionTime(order.getEstimatedCompletionTime())
                .estimatedPrice(order.getEstimatedPrice())
                .finalPrice(order.getFinalPrice())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .completedAt(order.getCompletedAt())
                .build();
    }

    private void updateFields(Order order, UpdateOrderRequestDTO update) {
        if (update.getItemName() != null) order.setItemName(update.getItemName());
        if (update.getItemCondition() != null) order.setItemCondition(update.getItemCondition());
        if (update.getRepairDetails() != null) order.setRepairDetails(update.getRepairDetails());
        if (update.getServiceDate() != null) order.setServiceDate(update.getServiceDate());
        if (update.getTechnicianId() != null) order.setTechnicianId(update.getTechnicianId());
        if (update.getPaymentMethodId() != null) order.setPaymentMethodId(update.getPaymentMethodId());
        if (update.getCouponId() != null) order.setCouponId(update.getCouponId());
        if (update.getEstimatedCompletionTime() != null) order.setEstimatedCompletionTime(update.getEstimatedCompletionTime());
        if (update.getEstimatedPrice() != null) order.setEstimatedPrice(update.getEstimatedPrice());
        if (update.getFinalPrice() != null) order.setFinalPrice(update  .getFinalPrice());
    }
}