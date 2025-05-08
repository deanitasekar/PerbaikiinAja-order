package id.ac.ui.cs.advprog.order.repository;
import id.ac.ui.cs.advprog.order.enums.OrderStatus;
import id.ac.ui.cs.advprog.order.model.Order;
import id.ac.ui.cs.advprog.order.model.OrderBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OrderRepository orderRepository;

    private UUID customerId;
    private UUID technicianId;
    private Order pendingOrder;
    private Order completedOrder;

    @BeforeEach
    void setUp() {
        customerId = UUID.randomUUID();
        technicianId = UUID.randomUUID();

        pendingOrder = new OrderBuilder()
                .setCustomerId(customerId)
                .setItemName("MacBook Pro 16-inch 2023")
                .setItemCondition("Cracked display panel with visible damage")
                .setRepairDetails("Full LCD replacement needed due to impact damage from a fall")
                .setDesiredServiceDate(new Date())
                .build();
        pendingOrder.setTechnicianId(technicianId);
        pendingOrder.setStatus(OrderStatus.PENDING);

        completedOrder = new OrderBuilder()
                .setCustomerId(customerId)
                .setItemName("Samsung Galaxy S23 Ultra")
                .setItemCondition("Original battery swollen, requires urgent replacement")
                .setRepairDetails("Battery drains within 30 minutes of use - diagnosed as faulty cell")
                .setDesiredServiceDate(new Date())
                .build();
        completedOrder.setTechnicianId(technicianId);
        completedOrder.setStatus(OrderStatus.COMPLETED);

        entityManager.persist(pendingOrder);
        entityManager.persist(completedOrder);
        entityManager.flush();
    }

    @Test
    void testFindByCustomerId() {
        List<Order> foundOrders = orderRepository.findByCustomerId(customerId);
        assertEquals(2, foundOrders.size());
        assertTrue(foundOrders.contains(pendingOrder));
        assertTrue(foundOrders.contains(completedOrder));
    }

    @Test
    void testFindByTechnicianId() {
        List<Order> technicianOrders = orderRepository.findByTechnicianId(technicianId);
        assertEquals(2, technicianOrders.size());
    }

    @Test
    void testFindByStatus() {
        List<Order> allPendingOrders = orderRepository.findByStatus(OrderStatus.PENDING);
        assertTrue(allPendingOrders.contains(pendingOrder));
        assertFalse(allPendingOrders.contains(completedOrder));
    }
}