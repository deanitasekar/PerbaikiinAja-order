package id.ac.ui.cs.advprog.order.repository;

import id.ac.ui.cs.advprog.order.model.Order;
import id.ac.ui.cs.advprog.order.model.OrderBuilder;
import id.ac.ui.cs.advprog.order.enums.OrderStatus;
import id.ac.ui.cs.advprog.order.enums.PaymentMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderRepositoryTest {

    private OrderRepository orderRepository;

    private Order sampleOrder1;
    private Order sampleOrder2;

    @BeforeEach
    void setUp() {
        orderRepository = new OrderRepository();

        sampleOrder1 = new OrderBuilder()
                .withOrderId("ORD-001")
                .withItemName("Laptop Rusak")
                .withItemCondition("Mati total")
                .withStatus(OrderStatus.PENDING)
                .withPaymentMethod(PaymentMethod.BANK_TRANSFER)
                .build();

        sampleOrder2 = new OrderBuilder()
                .withOrderId("ORD-002")
                .withItemName("HP Rusak")
                .withItemCondition("Retak Layar")
                .withStatus(OrderStatus.PENDING)
                .withPaymentMethod(PaymentMethod.CASH_ON_DELIVERY)
                .build();
    }

    @Test
    void testSaveNewOrder_Success() {
        orderRepository.save(sampleOrder1);

        Order found = orderRepository.findById("ORD-001");
        assertNotNull(found);
        assertEquals("Laptop Rusak", found.getItemName());
    }

    @Test
    void testSaveUpdateOrder_Success() {
        orderRepository.save(sampleOrder1);

        sampleOrder1.setItemName("Laptop Gaming Rusak");
        orderRepository.save(sampleOrder1);

        Order found = orderRepository.findById("ORD-001");
        assertNotNull(found);
        assertEquals("Laptop Gaming Rusak", found.getItemName());
    }

    @Test
    void testFindById_NotFound() {
        Exception ex = assertThrows(
                NoSuchElementException.class,
                () -> orderRepository.findById("ORD-999")
        );
        assertTrue(ex.getMessage().contains("No order found"));
    }

    @Test
    void testFindAll() {
        orderRepository.save(sampleOrder1);
        orderRepository.save(sampleOrder2);

        List<Order> all = orderRepository.findAll();
        assertEquals(2, all.size());
    }
}
