package id.ac.ui.cs.advprog.order.model;

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
        assertEquals("PENDING", order.getStatus());
    }

    @Test
    void testBuildOrder_EmptyName_ShouldThrowException() {
        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> new OrderBuilder()
                        .withItemName("")
                        .withItemCondition("Retak Layar")
                        .build()
        );
        assertTrue(ex.getMessage().contains("Nama barang tidak boleh kosong"));
    }

    @Test
    void testSetTechnician_Pending_Success() {
        order.setTechnician("Teknisi A");
        assertEquals("Teknisi A", order.getTechnician());
    }

    @Test
    void testSetTechnician_AfterApproved_ShouldFail() {
        order.setStatus("APPROVED");
        Exception ex = assertThrows(
                IllegalStateException.class,
                () -> order.setTechnician("Teknisi B")
        );
        assertTrue(ex.getMessage().contains("Tidak dapat diubah"));
    }

    @Test
    void testCancelOrder_WhenPending_Success() {
        order.cancel();
        assertEquals("CANCELLED", order.getStatus());
    }

    @Test
    void testCancelOrder_WhenApproved_ShouldFail() {
        order.setStatus("APPROVED");
        Exception ex = assertThrows(
                IllegalStateException.class,
                () -> order.cancel()
        );
        assertTrue(ex.getMessage().contains("Tidak dapat dibatalkan"));
    }
}
