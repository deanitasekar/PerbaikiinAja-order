package id.ac.ui.cs.advprog.order.model;

import id.ac.ui.cs.advprog.order.enums.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {

    private Order order;

    @BeforeEach
    void setUp() {
        order = new OrderBuilder()
                .withItemName("Laptop Rusak")
                .withItemCondition("Mati total")
                .build();
    }

    @Test
    void testBuildOrder_Success() {
        assertNotNull(order);
        assertEquals("Laptop Rusak", order.getItemName());
        assertEquals("Mati total", order.getItemCondition());
        assertEquals(OrderStatus.PENDING, order.getStatus());
    }

    @Test
    void testSetTechnician_Pending_Success() {
        order.setTechnician("Teknisi A");
        assertEquals("Teknisi A", order.getTechnician());
    }

    @Test
    void testSetTechnician_AfterApproved_ShouldFail() {
        order.setStatus("SUCCESS");
        Exception ex = assertThrows(
                IllegalStateException.class,
                () -> order.setTechnician("Teknisi B")
        );
        assertTrue(ex.getMessage().contains("Teknisi tidak dapat diubah"));
    }

    @Test
    void testCancelOrder_WhenPending_Success() {
        order.cancel();
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
    }

    @Test
    void testCancelOrder_WhenSuccess_ShouldFail() {
        order.setStatus("SUCCESS");
        Exception ex = assertThrows(
                IllegalStateException.class,
                () -> order.cancel()
        );
        assertTrue(ex.getMessage().contains("Order tidak dapat dibatalkan"));
    }
}
