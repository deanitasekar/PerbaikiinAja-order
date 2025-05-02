package id.ac.ui.cs.advprog.order.repository;

import id.ac.ui.cs.advprog.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByCustomerId(UUID customerId);
    List<Order> findByCustomerIdAndStatus(UUID customerId, String status);
    List<Order> findByTechnicianId(UUID technicianId);
    List<Order> findByTechnicianIdAndStatus(UUID technicianId, String status);
    List<Order> findByStatus(String status);
    Optional<Order> findById(UUID orderId);
}