package id.ac.ui.cs.advprog.order.service;

import id.ac.ui.cs.advprog.order.dto.OrderRequestDTO;
import id.ac.ui.cs.advprog.order.dto.UpdateOrderRequestDTO;
import id.ac.ui.cs.advprog.order.dto.OrderResponseDTO;
import id.ac.ui.cs.advprog.order.dto.OrderListResponseDTO;
import id.ac.ui.cs.advprog.order.dto.ResponseDTO;
import id.ac.ui.cs.advprog.order.enums.OrderStatus;
import id.ac.ui.cs.advprog.order.model.Order;
import id.ac.ui.cs.advprog.order.model.OrderBuilder;
import id.ac.ui.cs.advprog.order.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequest) {
        Order order = new OrderBuilder()
                .setCustomerId(orderRequest.getCustomerId())
                .setItemName(orderRequest.getItemName())
                .setItemCondition(orderRequest.getItemCondition())
                .setRepairDetails(orderRequest.getRepairDetails())
                .setServiceDate(orderRequest.getServiceDate())
                .build();

        if (orderRequest.getTechnicianId() != null) {
            order.setTechnicianId(orderRequest.getTechnicianId());
        }

        Order savedOrder = orderRepository.save(order);

        return convertToOrderResponse(savedOrder, orderRequest.getPaymentMethod());
    }

    @Override
    public OrderResponseDTO getOrderById(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + orderId));

        return convertToOrderResponse(order, null);
    }

    @Override
    public OrderListResponseDTO getOrdersByCustomerId(UUID customerId) {
        List<Order> orders = orderRepository.findByCustomerId(customerId);

        List<OrderResponseDTO> orderResponses = orders.stream()
                .map(order -> convertToOrderResponse(order, null))
                .collect(Collectors.toList());

        return OrderListResponseDTO.builder()
                .orders(orderResponses)
                .count(orderResponses.size())
                .build();
    }


    @Override
    public OrderResponseDTO updateOrder(UUID orderId, UpdateOrderRequestDTO updateOrderRequest) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + orderId));

        if (order.getStatus() != OrderStatus.PENDING && order.getStatus() != OrderStatus.WAITING_APPROVAL) {
            throw new IllegalStateException("Order cannot be updated after technician has reviewed it");
        }

        if (updateOrderRequest.getItemName() != null) {
            order.setItemName(updateOrderRequest.getItemName());
        }

        if (updateOrderRequest.getItemCondition() != null) {
            order.setItemCondition(updateOrderRequest.getItemCondition());
        }

        if (updateOrderRequest.getRepairDetails() != null) {
            order.setIssueDescription(updateOrderRequest.getRepairDetails());
        }

        if (updateOrderRequest.getServiceDate() != null) {
            order.setServiceDate(updateOrderRequest.getServiceDate());
        }

        if (updateOrderRequest.getTechnicianId() != null) {
            order.setTechnicianId(updateOrderRequest.getTechnicianId());
        }

        order.setUpdatedAt(LocalDateTime.now());

        Order updatedOrder = orderRepository.save(order);

        return convertToOrderResponse(updatedOrder, updateOrderRequest.getPaymentMethod());
    }

    @Override
    public ResponseDTO cancelOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + orderId));

        if (order.getStatus() != OrderStatus.PENDING && order.getStatus() != OrderStatus.WAITING_APPROVAL) {
            throw new IllegalStateException("Order cannot be cancelled after technician has approved it");
        }

        order.setStatus(OrderStatus.CANCELLED);
        order.setUpdatedAt(LocalDateTime.now());

        orderRepository.save(order);

        return ResponseDTO.builder()
                .success(true)
                .message("Order has been successfully cancelled")
                .build();
    }

    private OrderResponseDTO convertToOrderResponse(Order order, String paymentMethod) {
        OrderResponseDTO responseDTO = new OrderResponseDTO();
        responseDTO.setId(order.getId());
        responseDTO.setCustomerId(order.getCustomerId());
        responseDTO.setTechnicianId(order.getTechnicianId());
        responseDTO.setItemName(order.getItemName());
        responseDTO.setItemCondition(order.getItemCondition());
        responseDTO.setRepairDetails(order.getIssueDescription());
        responseDTO.setServiceDate(order.getServiceDate());
        responseDTO.setStatus(order.getStatus());
        responseDTO.setRepairEstimate(order.getRepairEstimate());
        responseDTO.setRepairPrice(order.getRepairPrice());
        responseDTO.setRepairReport(order.getRepairReport());
        responseDTO.setCreatedAt(order.getCreatedAt());
        responseDTO.setUpdatedAt(order.getUpdatedAt());

        if (paymentMethod != null) {
            responseDTO.setPaymentMethod(paymentMethod);
        }

        return responseDTO;
    }
}