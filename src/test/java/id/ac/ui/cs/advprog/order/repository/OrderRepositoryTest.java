package id.ac.ui.cs.advprog.order.repository;

import static org.junit.jupiter.api.Assertions.*;
import id.ac.ui.cs.advprog.order.model.Order;
import id.ac.ui.cs.advprog.order.model.OrderBuilder;
import id.ac.ui.cs.advprog.order.enums.OrderStatus;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@DataJpaTest
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void testSaveAndFindById() {
        UUID customerId = UUID.randomUUID();
        OrderBuilder builder = new OrderBuilder(
                customerId,
                "Laptop",
                "Screen flickering",
                "Fix screen display issue",
                new Date(),
                PaymentMethod.BANK_TRANSFER
        );

        Order savedOrder = orderRepository.save(builder.build());
        Optional<Order> foundOrder = orderRepository.findById(savedOrder.getId());

        assertTrue(foundOrder.isPresent());
        assertEquals("Laptop", foundOrder.get().getItemName());
        assertEquals(OrderStatus.PENDING.name(), foundOrder.get().getStatus());
    }

    @Test
    void testFindByCustomerId() {
        UUID customerId = UUID.randomUUID();
        orderRepository.save(new OrderBuilder(
                customerId,
                "Smartphone",
                "Battery issue",
                "Replace battery",
                new Date(),
                PaymentMethod.E_WALLET
        ).build());

        List<Order> orders = orderRepository.findByCustomerId(customerId);

        assertEquals(1, orders.size());
        assertEquals(customerId, orders.get(0).getCustomerId());
    }
    @Test
    void testFindByCustomerIdAndStatus() {
        UUID customerId = UUID.randomUUID();
        OrderStatus targetStatus = OrderStatus.APPROVED;

        Order order = new OrderBuilder(
                customerId,
                "Refrigerator",
                "Not cooling",
                "Repair cooling system",
                new Date(),
                PaymentMethod.BANK_TRANSFER
        )
                .setStatus(targetStatus)
                .build();

        orderRepository.save(order);

        List<Order> results = orderRepository.findByCustomerIdAndStatus(
                customerId,
                targetStatus.name()
        );

        assertEquals(1, results.size());
        assertEquals(targetStatus.name(), results.get(0).getStatus());
    }

    @Test
    void testFindByTechnicianIdAndStatus() {
        UUID technicianId = UUID.randomUUID();
        String targetStatus = OrderStatus.APPROVED.name();

        Order order = new OrderBuilder(
                UUID.randomUUID(),
                "Washing Machine",
                "Leaking",
                "Fix water leakage",
                new Date(),
                PaymentMethod.CASH_ON_DELIVERY
        )
                .setTechnicianId(technicianId)
                .setStatus(OrderStatus.APPROVED)
                .build();

        orderRepository.save(order);

        List<Order> results = orderRepository.findByTechnicianIdAndStatus(
                technicianId,
                targetStatus
        );

        assertEquals(1, results.size());
        assertEquals(technicianId, results.get(0).getTechnicianId());
        assertEquals(targetStatus, results.get(0).getStatus());
    }

    @Test
    void testFindByStatus() {
        OrderStatus targetStatus = OrderStatus.COMPLETED;

        Order order1 = new OrderBuilder(
                UUID.randomUUID(),
                "TV",
                "No power",
                "Repair power supply",
                new Date(),
                PaymentMethod.BANK_TRANSFER
        )
                .setStatus(targetStatus)
                .build();

        Order order2 = new OrderBuilder(
                UUID.randomUUID(),
                "AC",
                "Not cooling",
                "Refill coolant",
                new Date(),
                PaymentMethod.E_WALLET
        )
                .setStatus(OrderStatus.PENDING)
                .build();

        orderRepository.saveAll(List.of(order1, order2));

        List<Order> results = orderRepository.findByStatus(targetStatus.name());

        assertEquals(1, results.size());
        assertEquals(targetStatus.name(), results.get(0).getStatus());
    }

    @Test
    void testFindByTechnicianId() {
        UUID technicianId = UUID.randomUUID();
        Order order = new OrderBuilder(
                UUID.randomUUID(),
                "Microwave",
                "Not heating",
                "Check magnetron",
                new Date(),
                PaymentMethod.E_WALLET
        )
                .setTechnicianId(technicianId)
                .build();

        orderRepository.save(order);

        List<Order> results = orderRepository.findByTechnicianId(technicianId);

        assertEquals(1, results.size());
        assertEquals(technicianId, results.get(0).getTechnicianId());
    }
}