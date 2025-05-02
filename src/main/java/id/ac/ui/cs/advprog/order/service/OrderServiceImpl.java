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
        this.orderRepository = orderRepository;
    }

    @Override
    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getCustomerOrders(UUID customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    @Override
    public Order findById(UUID orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        return orderOptional.orElse(null);
    }

    @Override
    public Order updateStatus(UUID orderId, String newStatus) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isEmpty()) {
            throw new IllegalArgumentException("Order not found with ID: " + orderId);
        }
        Order order = orderOptional.get();
        order.setStatus(newStatus);
        return orderRepository.save(order);
    }

    @Override
    public Order updateOrderDetails(UUID orderId, Order updatedOrder) throws Exception {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isEmpty()) {
            throw new IllegalArgumentException("Order not found with ID: " + orderId);
        }
        Order existingOrder = orderOptional.get();
        if (!existingOrder.canBeUpdated()) {
            throw new IllegalStateException("Order cannot be updated in current status: " + existingOrder.getStatus());
        }
        existingOrder.setItemName(updatedOrder.getItemName());
        existingOrder.setItemCondition(updatedOrder.getItemCondition());
        existingOrder.setRepairRequest(updatedOrder.getRepairRequest());
        existingOrder.setServiceDate(updatedOrder.getServiceDate());
        existingOrder.setPaymentMethod(updatedOrder.getPaymentMethod());
        existingOrder.setUsingCoupon(updatedOrder.isUsingCoupon());
        return orderRepository.save(existingOrder);
    }

    @Override
    public Order changeTechnician(UUID orderId, UUID newTechnicianId) throws Exception {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isEmpty()) {
            throw new IllegalArgumentException("Order not found with ID: " + orderId);
        }
        Order order = orderOptional.get();
        order.updateTechnician(newTechnicianId);
        return orderRepository.save(order);
    }

    @Override
    public void cancelOrder(UUID orderId, UUID userId) throws Exception {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isEmpty()) {
            throw new IllegalArgumentException("Order not found with ID: " + orderId);
        }
        Order order = orderOptional.get();
        if (!order.getCustomerId().equals(userId)) {
            throw new IllegalStateException("User is not authorized to cancel this order.");
        }
        order.cancel();
        orderRepository.save(order);
    }

    @Override
    public Order completeOrder(UUID orderId, UUID technicianId, String repairReport) throws Exception {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isEmpty()) {
            throw new IllegalArgumentException("Order not found with ID: " + orderId);
        }
        Order order = orderOptional.get();
        if (!order.getTechnicianId().equals(technicianId)) {
            throw new IllegalStateException("Technician is not authorized to complete this order.");
        }
        order.complete();
        order.setRepairReport(repairReport);
        return orderRepository.save(order);
    }
}