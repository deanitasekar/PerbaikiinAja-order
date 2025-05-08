package id.ac.ui.cs.advprog.order.model;

import id.ac.ui.cs.advprog.order.enums.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


class OrderTest {

    private Order order;
    private OrderBuilder orderBuilder;
    private UUID customerId;
    private Date serviceDate;

    @BeforeEach
    void setUp() {
        customerId = UUID.randomUUID();
        serviceDate = new Date();

        this.orderBuilder = new OrderBuilder()
                .setCustomerId(customerId)
                .setItemName("ASUS ROG Zephyrus G14")
                .setItemCondition("Second-hand, 2 years of usage")
                .setRepairDetails("Device fails to power on despite charging")
                .setServiceDate(serviceDate);

        this.order = orderBuilder.build();
    }

    @Test
    void testOrderCreation() {
        assertNotNull(this.order);
        assertEquals(customerId, this.order.getCustomerId());
        assertEquals("ASUS ROG Zephyrus G14", this.order.getItemName());
        assertEquals("Second-hand, 2 years of usage", this.order.getItemCondition());
        assertEquals("Device fails to power on despite charging", this.order.getIssueDescription());
        assertEquals(serviceDate, this.order.getServiceDate());
        assertEquals(OrderStatus.PENDING, this.order.getStatus());
        assertNotNull(this.order.getCreatedAt());
        assertNotNull(this.order.getUpdatedAt());
    }

    @Test
    void testGetOrderStatusDefault() {
        assertEquals(OrderStatus.PENDING, this.order.getStatus());
    }

    @Test
    void testSetOrderStatus() {
        OrderStatus newStatus = OrderStatus.IN_PROGRESS;
        this.order.setStatus(newStatus);
        assertEquals(OrderStatus.IN_PROGRESS, this.order.getStatus());
    }

    @Test
    void testGetCustomerId() {
        assertEquals(customerId, this.order.getCustomerId());
    }

    @Test
    void testSetTechnicianId() {
        UUID technicianId = UUID.randomUUID();
        this.order.setTechnicianId(technicianId);
        assertEquals(technicianId, this.order.getTechnicianId());
    }

    @Test
    void testSetRepairEstimate() {
        String estimate = "Requires new power supply. Estimated time: 2 days";
        this.order.setRepairEstimate(estimate);
        assertEquals(estimate, this.order.getRepairEstimate());
    }

    @Test
    void testSetRepairPrice() {
        Double price = 150.50;
        this.order.setRepairPrice(price);
        assertEquals(price, this.order.getRepairPrice());
    }

    @Test
    void testSetRepairReport() {
        String report = "Replaced power supply and cleaned dust from fans";
        this.order.setRepairReport(report);
        assertEquals(report, this.order.getRepairReport());
    }

    @Test
    void testUpdatedAtChanges() {
        LocalDateTime initialUpdatedAt = this.order.getUpdatedAt();

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.order.setStatus(OrderStatus.COMPLETED);
        this.order.setUpdatedAt(LocalDateTime.now());

        assertNotEquals(initialUpdatedAt, this.order.getUpdatedAt());
    }

    @Test
    void testOrderBuilderChaining() {
        UUID newCustomerId = UUID.randomUUID();
        String newItemName = "Phone";
        String newCondition = "Like new, minor cosmetic scratches";
        String newDetails = "Screen flickering intermittently";
        Date newDate = new Date(System.currentTimeMillis() + 86400000);

        OrderBuilder builder = new OrderBuilder()
                .setCustomerId(newCustomerId)
                .setItemName(newItemName)
                .setItemCondition(newCondition)
                .setRepairDetails(newDetails)
                .setServiceDate(newDate);

        Order newOrder = builder.build();

        assertEquals(newCustomerId, newOrder.getCustomerId());
        assertEquals(newItemName, newOrder.getItemName());
        assertEquals(newCondition, newOrder.getItemCondition());
        assertEquals(newDetails, newOrder.getIssueDescription());
        assertEquals(newDate, newOrder.getServiceDate());
    }

    @Test
    void testOrderStatusValues() {
        assertEquals(7, OrderStatus.values().length);
        assertEquals("PENDING", OrderStatus.PENDING.name());
        assertEquals("WAITING_APPROVAL", OrderStatus.WAITING_APPROVAL.name());
        assertEquals("APPROVED", OrderStatus.APPROVED.name());
        assertEquals("IN_PROGRESS", OrderStatus.IN_PROGRESS.name());
        assertEquals("COMPLETED", OrderStatus.COMPLETED.name());
        assertEquals("REJECTED", OrderStatus.REJECTED.name());
        assertEquals("CANCELLED", OrderStatus.CANCELLED.name());
    }

    @Test
    void testOrderStatusGetValue() {
        assertEquals("PENDING", OrderStatus.PENDING.getValue());
        assertEquals("WAITING_APPROVAL", OrderStatus.WAITING_APPROVAL.getValue());
        assertEquals("APPROVED", OrderStatus.APPROVED.getValue());
        assertEquals("IN_PROGRESS", OrderStatus.IN_PROGRESS.getValue());
        assertEquals("COMPLETED", OrderStatus.COMPLETED.getValue());
        assertEquals("REJECTED", OrderStatus.REJECTED.getValue());
        assertEquals("CANCELLED", OrderStatus.CANCELLED.getValue());
    }

    @Test
    void testOrderStatusContains() {
        assertTrue(OrderStatus.contains("PENDING"));
        assertTrue(OrderStatus.contains("WAITING_APPROVAL"));
        assertTrue(OrderStatus.contains("APPROVED"));
        assertTrue(OrderStatus.contains("IN_PROGRESS"));
        assertTrue(OrderStatus.contains("COMPLETED"));
        assertTrue(OrderStatus.contains("REJECTED"));
        assertTrue(OrderStatus.contains("CANCELLED"));

        assertFalse(OrderStatus.contains("UNKNOWN"));
        assertFalse(OrderStatus.contains("pending"));
        assertFalse(OrderStatus.contains(""));
        assertFalse(OrderStatus.contains(null));
    }

    @Test
    void testOrderStatusFromValue() {
        assertEquals(OrderStatus.PENDING, OrderStatus.fromValue("PENDING"));
        assertEquals(OrderStatus.WAITING_APPROVAL, OrderStatus.fromValue("WAITING_APPROVAL"));
        assertEquals(OrderStatus.APPROVED, OrderStatus.fromValue("APPROVED"));
        assertEquals(OrderStatus.IN_PROGRESS, OrderStatus.fromValue("IN_PROGRESS"));
        assertEquals(OrderStatus.COMPLETED, OrderStatus.fromValue("COMPLETED"));
        assertEquals(OrderStatus.REJECTED, OrderStatus.fromValue("REJECTED"));
        assertEquals(OrderStatus.CANCELLED, OrderStatus.fromValue("CANCELLED"));
    }

    @Test
    void testOrderStatusFromValueInvalid() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            OrderStatus.fromValue("UNKNOWN");
        });

        assertTrue(exception.getMessage().contains("Invalid order status"));
    }
}