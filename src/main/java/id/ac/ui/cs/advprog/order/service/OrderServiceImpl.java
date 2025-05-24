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

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Async
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public CompletableFuture<OrderResponseDTO> createOrder(OrderRequestDTO request) {
        return CompletableFuture.supplyAsync(() -> {
            Order order = new OrderBuilder()
                    .setCustomerId(request.getCustomerId())
                    .setTechnicianId(request.getTechnicianId())
                    .setItemName(request.getItemName())
                    .setItemCondition(request.getItemCondition())
                    .setRepairDetails(request.getRepairDetails())
                    .setServiceDate(request.getServiceDate())
                    .setPaymentMethodId(request.getPaymentMethodId())
                    .setCouponId(request.getCouponId())
                    .build();
            Order saved = orderRepository.save(order);
            return mapToDTO(saved);
        });
    }

    @Override
    public CompletableFuture<OrderResponseDTO> getOrderById(UUID orderId) {
        return CompletableFuture.supplyAsync(() -> {
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new EntityNotFoundException("Order not found: " + orderId));
            return mapToDTO(order);
        });
    }

    @Override
    public CompletableFuture<OrderListResponseDTO> getOrdersByCustomerId(UUID customerId) {
        return CompletableFuture.supplyAsync(() -> {
            List<OrderResponseDTO> dtos = orderRepository.findByCustomerId(customerId)
                    .stream()
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
            return OrderListResponseDTO.builder()
                    .orders(dtos)
                    .count(dtos.size())
                    .build();
        });
    }

    @Override
    public CompletableFuture<OrderListResponseDTO> getAllOrders() {
        return CompletableFuture.supplyAsync(() -> {
            List<OrderResponseDTO> dtos = orderRepository.findAll()
                    .stream()
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());

            return OrderListResponseDTO.builder()
                    .orders(dtos)
                    .count(dtos.size())
                    .build();
        });
    }

    @Override
    public CompletableFuture<OrderResponseDTO> updateOrder(UUID orderId, UpdateOrderRequestDTO update) {
        return CompletableFuture.supplyAsync(() -> {
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new EntityNotFoundException("Order not found: " + orderId));

            boolean changeTech = update.getTechnicianId() != null;
            boolean changeItem = update.getItemName() != null
                    || update.getItemCondition() != null
                    || update.getRepairDetails() != null;

            if ((changeTech || changeItem) && !(order.getStatus() == OrderStatus.PENDING
                    || order.getStatus() == OrderStatus.WAITING_APPROVAL)) {
                throw new IllegalStateException(
                        "Cannot change order when status is " + order.getStatus());
            }

            updateFields(order, update);
            Order updated = orderRepository.save(order);
            return mapToDTO(updated);
        });
    }

    @Override
    public CompletableFuture<ResponseDTO> cancelOrder(UUID orderId) {
        return CompletableFuture.supplyAsync(() -> {
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new EntityNotFoundException("Order not found: " + orderId));
            order.setStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);
            return ResponseDTO.builder()
                    .success(true)
                    .message("Order cancelled: " + order.getId())
                    .build();
        });
    }

    private void updateFields(Order order, UpdateOrderRequestDTO update) {
        if (update.getItemName() != null)       order.setItemName(update.getItemName());
        if (update.getItemCondition() != null)  order.setItemCondition(update.getItemCondition());
        if (update.getRepairDetails() != null)  order.setRepairDetails(update.getRepairDetails());
        if (update.getServiceDate() != null)    order.setServiceDate(update.getServiceDate());
        if (update.getTechnicianId() != null)   order.setTechnicianId(update.getTechnicianId());
        if (update.getPaymentMethodId() != null)order.setPaymentMethodId(update.getPaymentMethodId());
        if (update.getCouponId() != null)       order.setCouponId(update.getCouponId());
        if (update.getEstimatedCompletionTime() != null)
            order.setEstimatedCompletionTime(update.getEstimatedCompletionTime());
        if (update.getEstimatedPrice() != null) order.setEstimatedPrice(update.getEstimatedPrice());
        if (update.getFinalPrice() != null)     order.setFinalPrice(update.getFinalPrice());
        if (update.getCompletedAt() != null)    order.setCompletedAt(update.getCompletedAt());
    }

    private OrderResponseDTO mapToDTO(Order order) {
        return new OrderResponseDTO(
                order.getId(),
                order.getCustomerId(),
                order.getTechnicianId(),
                order.getItemName(),
                order.getItemCondition(),
                order.getRepairDetails(),
                order.getServiceDate(),
                order.getStatus(),
                order.getPaymentMethodId(),
                order.getCouponId(),
                order.getEstimatedCompletionTime(),
                order.getEstimatedPrice(),
                order.getFinalPrice(),
                order.getCreatedAt(),
                order.getUpdatedAt(),
                order.getCompletedAt()
        );
    }
}