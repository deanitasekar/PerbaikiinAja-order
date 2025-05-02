package id.ac.ui.cs.advprog.order.service;

import id.ac.ui.cs.advprog.order.model.Order;
import id.ac.ui.cs.advprog.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
    }

    @Override
    public Order createOrder(Order order) {
    }

    @Override
    public List<Order> getCustomerOrders(UUID customerId) {
    }

    @Override
    public Order findById(UUID orderId) {
    }

    @Override
    public Order updateStatus(UUID orderId, String newStatus) {
    }

    @Override
    public Order updateOrderDetails(UUID orderId, Order updatedOrder) throws Exception {
    }

    @Override
    public Order changeTechnician(UUID orderId, UUID newTechnicianId) throws Exception {
    }

    @Override
    public void cancelOrder(UUID orderId, UUID userId) throws Exception {
    }

    @Override
    public Order completeOrder(UUID orderId, UUID technicianId, String repairReport) throws Exception {
    }
}