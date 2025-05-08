package id.ac.ui.cs.advprog.order.service;

import id.ac.ui.cs.advprog.order.dto.OrderRequestDTO;
import id.ac.ui.cs.advprog.order.dto.UpdateOrderRequestDTO;
import id.ac.ui.cs.advprog.order.dto.OrderResponseDTO;
import id.ac.ui.cs.advprog.order.dto.OrderListResponseDTO;
import id.ac.ui.cs.advprog.order.dto.ResponseDTO;
import id.ac.ui.cs.advprog.order.model.Order;
import id.ac.ui.cs.advprog.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequest) {
    }

    @Override
    public OrderResponseDTO getOrderById(UUID orderId) {
    }

    @Override
    public OrderListResponseDTO getOrdersByCustomerId(UUID customerId) {
    }


    @Override
    public OrderResponseDTO updateOrder(UUID orderId, UpdateOrderRequestDTO updateOrderRequest) {
    }

    @Override
    public ResponseDTO cancelOrder(UUID orderId) {
    }

    private OrderResponseDTO convertToOrderResponse(Order order, String paymentMethod) {
    }
}