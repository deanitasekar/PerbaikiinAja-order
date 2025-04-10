package id.ac.ui.cs.advprog.order.service;

import id.ac.ui.cs.advprog.order.model.Order;

public interface OrderService {
    Order createOrder(Order order);
    Order updateStatus(String orderId, String newStatus);
    Order findById(String orderId);
}