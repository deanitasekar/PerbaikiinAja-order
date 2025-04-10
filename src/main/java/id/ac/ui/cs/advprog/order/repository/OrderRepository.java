package id.ac.ui.cs.advprog.order.repository;

import id.ac.ui.cs.advprog.order.model.Order;

import java.util.List;
import java.util.NoSuchElementException;

public class OrderRepository {

    public void save(Order order) {
    }

    public Order findById(String orderId) {
        throw new NoSuchElementException("No order found with id: " + orderId);
    }

    public List<Order> findAll() {
        return null;
    }
}
