package id.ac.ui.cs.advprog.order.service;

import id.ac.ui.cs.advprog.order.model.Order;
import id.ac.ui.cs.advprog.order.repository.OrderRepository;

public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order createOrder(Order order) {
        orderRepository.save(order);
        return order;
    }

    @Override
    public Order updateStatus(String orderId, String newStatus) {
        Order existing = orderRepository.findById(orderId);
        existing.setStatus(newStatus);
        orderRepository.save(existing);
        return existing;
    }

    @Override
    public Order findById(String orderId) {
        return orderRepository.findById(orderId);
    }
}
