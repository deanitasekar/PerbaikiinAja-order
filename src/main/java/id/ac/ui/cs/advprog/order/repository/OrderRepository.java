package id.ac.ui.cs.advprog.order.repository;

import id.ac.ui.cs.advprog.order.model.Order;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class OrderRepository {

    private final Map<String, Order> orderData = new HashMap<>();

    public void save(Order order) {
        orderData.put(order.getOrderId(), order);
    }

    public Order findById(String orderId) {
        if (!orderData.containsKey(orderId)) {
            throw new NoSuchElementException("No order found");
        }
        return orderData.get(orderId);
    }

    public List<Order> findAll() {
        return new ArrayList<>(orderData.values());
    }
}
