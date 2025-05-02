test model OrderTest
        package id.ac.ui.cs.advprog.order.model;

import id.ac.ui.cs.advprog.order.enums.OrderStatus;
import id.ac.ui.cs.advprog.order.enums.PaymentMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    private OrderBuilder orderBuilder;
    private final Date serviceDate = new Date();
    private final String customerId = "customer123";
    private final String itemName = "Laptop";
    private final String itemCondition = "Screen broken";
    private final String repairRequest = "Fix screen";
    private final String technicianId = "tech456";

    @BeforeEach
    void setUp() {
        orderBuilder = new OrderBuilder(
                customerId,
                itemName,
                itemCondition,
                repairRequest,
                serviceDate,
                PaymentMethod.BANK_TRANSFER
        );
    }

    @Test
    void testOrderCreation() {
        Order order = orderBuilder.build();

        assertNotNull(order.getId());
        assertEquals(customerId, order.getCustomerId());
        assertEquals(itemName, order.getItemName());
        assertEquals(itemCondition, order.getItemCondition());
        assertEquals(repairRequest, order.getRepairRequest());
        assertEquals(serviceDate, order.getServiceDate());
        assertEquals(PaymentMethod.BANK_TRANSFER.getValue(), order.getPaymentMethod());
        assertEquals(OrderStatus.PENDING.name(), order.getStatus());
        assertFalse(order.isUsingCoupon());
        assertNull(order.getCouponCode());
        assertNotNull(order.getCreatedAt());
        assertNotNull(order.getUpdatedAt());
    }

    @Test
    void testOrderCreationWithAllParameters() {
        Order order = orderBuilder
                .setTechnicianId(technicianId)
                .setPaymentMethod(PaymentMethod.E_WALLET)
                .setCustomPaymentDetails("GoPay: 081234567890")
                .setUsingCoupon(true)
                .setCouponCode("DISCOUNT50")
                .build();

        assertEquals(technicianId, order.getTechnicianId());
        assertEquals(PaymentMethod.E_WALLET.getValue(), order.getPaymentMethod());
        assertEquals("GoPay: 081234567890", order.getPaymentDetails());
        assertTrue(order.isUsingCoupon());
        assertEquals("DISCOUNT50", order.getCouponCode());
        assertEquals(OrderStatus.WAITING_APPROVAL.name(), order.getStatus());
    }

    @Test
    void testNoArgsConstructor() {
        Order order = new Order();
        assertNull(order.getId());
        assertNull(order.getStatus());
    }

    @ParameterizedTest
    @EnumSource(OrderStatus.class)
    void testSetAllOrderStatus(OrderStatus status) {
        Order order = orderBuilder.build();
        order.setStatus(status.name());
        assertEquals(status.name(), order.getStatus());
    }

    @Test
    void testSetInvalidStatus() {
        Order order = orderBuilder.build();
        assertThrows(IllegalArgumentException.class, () -> order.setStatus("INVALID_STATUS"));
    }

    @ParameterizedTest
    @EnumSource(PaymentMethod.class)
    void testSetAllPaymentMethods(PaymentMethod method) {
        Order order = orderBuilder.build();
        order.setPaymentMethod(method.getValue());
        assertEquals(method.getValue(), order.getPaymentMethod());
    }

    @Test
    void testSetInvalidPaymentMethod() {
        Order order = orderBuilder.build();
        assertThrows(IllegalArgumentException.class, () -> order.setPaymentMethod("BITCOIN"));
    }

    @Test
    void testOrderStatusEnumConversion() {
        Order order = orderBuilder.build();
        assertEquals(OrderStatus.PENDING, order.getOrderStatus());

        order.setStatus(OrderStatus.APPROVED.name());
        assertEquals(OrderStatus.APPROVED, order.getOrderStatus());
    }

    @Test
    void testPaymentMethodEnumConversion() {
        Order order = orderBuilder.build();
        assertEquals(PaymentMethod.BANK_TRANSFER, order.getPaymentMethodEnum());

        order.setPaymentMethod(PaymentMethod.CASH_ON_DELIVERY.getValue());
        assertEquals(PaymentMethod.CASH_ON_DELIVERY, order.getPaymentMethodEnum());
    }

    @Test
    void testCanBeUpdated() {
        Order order = orderBuilder.build();
        assertTrue(order.canBeUpdated());

        order = orderBuilder.setStatus(OrderStatus.WAITING_APPROVAL).build();
        assertTrue(order.canBeUpdated());

        order = orderBuilder.setStatus(OrderStatus.APPROVED).build();
        assertFalse(order.canBeUpdated());
    }

    @Test
    void testCanBeCancelled() {
        Order order = orderBuilder.build();
        assertTrue(order.canBeCancelled());

        order = orderBuilder.setStatus(OrderStatus.WAITING_APPROVAL).build();
        assertTrue(order.canBeCancelled());

        order = orderBuilder.setStatus(OrderStatus.APPROVED).build();
        assertFalse(order.canBeCancelled());
    }

    @Test
    void testUpdateTechnicianSuccess() {
        Order order = orderBuilder.build();
        order.updateTechnician("newTech123");

        assertEquals("newTech123", order.getTechnicianId());
        assertEquals(OrderStatus.WAITING_APPROVAL.name(), order.getStatus());
    }

    @Test
    void testUpdateTechnicianWithInvalidState() {
        Order order = orderBuilder.setStatus(OrderStatus.APPROVED).build();
        assertThrows(IllegalStateException.class, () -> order.updateTechnician("newTech123"));
    }

    @Test
    void testCancelSuccess() {
        Order order = orderBuilder.build();
        order.cancel();

        assertEquals(OrderStatus.CANCELLED.name(), order.getStatus());
    }

    @Test
    void testCancelWithInvalidState() {
        Order order = orderBuilder.setStatus(OrderStatus.APPROVED).build();
        assertThrows(IllegalStateException.class, () -> order.cancel());
    }

    @Test
    void testApproveSuccess() {
        Order order = orderBuilder.setTechnicianId(technicianId).build();
        order.approve();

        assertEquals(OrderStatus.APPROVED.name(), order.getStatus());
    }

    @Test
    void testApproveWithInvalidState() {
        Order order = orderBuilder.build();
        assertThrows(IllegalStateException.class, () -> order.approve());
    }

    @Test
    void testRejectSuccess() {
        Order order = orderBuilder.setTechnicianId(technicianId).build();
        order.reject();

        assertEquals(OrderStatus.REJECTED.name(), order.getStatus());
    }

    @Test
    void testRejectWithInvalidState() {
        Order order = orderBuilder.build();
        assertThrows(IllegalStateException.class, () -> order.reject());
    }

    @Test
    void testStartProgressSuccess() {
        Order order = orderBuilder.setStatus(OrderStatus.APPROVED).build();
        order.startProgress();

        assertEquals(OrderStatus.IN_PROGRESS.name(), order.getStatus());
    }

    @Test
    void testStartProgressWithInvalidState() {
        Order order = orderBuilder.build();
        assertThrows(IllegalStateException.class, () -> order.startProgress());
    }

    @Test
    void testCompleteSuccess() {
        Order order = orderBuilder.setStatus(OrderStatus.IN_PROGRESS).build();
        order.complete();

        assertEquals(OrderStatus.COMPLETED.name(), order.getStatus());
    }

    @Test
    void testCompleteWithInvalidState() {
        Order order = orderBuilder.build();
        assertThrows(IllegalStateException.class, () -> order.complete());
    }

    @Test
    void testSetRepairEstimateSuccess() {
        Order order = orderBuilder.setTechnicianId(technicianId).build();
        order.setRepairEstimate("2 days");

        assertEquals("2 days", order.getRepairEstimate());
    }

    @Test
    void testSetRepairEstimateWithInvalidState() {
        Order order = orderBuilder.build();
        assertThrows(IllegalStateException.class, () -> order.setRepairEstimate("2 days"));
    }

    @Test
    void testSetRepairPriceSuccess() {
        Order order = orderBuilder.setTechnicianId(technicianId).build();
        order.setRepairPrice(150.0);

        assertEquals(150.0, order.getRepairPrice());
    }

    @Test
    void testSetRepairPriceWithInvalidState() {
        Order order = orderBuilder.build();
        assertThrows(IllegalStateException.class, () -> order.setRepairPrice(150.0));
    }

    @Test
    void testSetRepairReportSuccess() {
        Order order = orderBuilder.setStatus(OrderStatus.COMPLETED).build();
        order.setRepairReport("Screen replaced with new one");

        assertEquals("Screen replaced with new one", order.getRepairReport());
    }

    @Test
    void testSetRepairReportWithInvalidState() {
        Order order = orderBuilder.build();
        assertThrows(IllegalStateException.class, () -> order.setRepairReport("Screen replaced"));
    }

    @Test
    void testSettersAndGetters() {
        Order order = new Order();

        String id = UUID.randomUUID().toString();
        order.setId(id);
        assertEquals(id, order.getId());

        order.setCustomerId("newCustomer123");
        assertEquals("newCustomer123", order.getCustomerId());

        order.setItemName("New Laptop");
        assertEquals("New Laptop", order.getItemName());

        order.setItemCondition("Battery issues");
        assertEquals("Battery issues", order.getItemCondition());

        order.setRepairRequest("Replace battery");
        assertEquals("Replace battery", order.getRepairRequest());

        Date newDate = new Date(serviceDate.getTime() + 86400000);
        order.setServiceDate(newDate);
        assertEquals(newDate, order.getServiceDate());

        order.setPaymentDetails("Bank transfer to XYZ Bank");
        assertEquals("Bank transfer to XYZ Bank", order.getPaymentDetails());

        order.setUsingCoupon(true);
        assertTrue(order.isUsingCoupon());

        order.setCouponCode("NEW_COUPON");
        assertEquals("NEW_COUPON", order.getCouponCode());

        Date createdAt = new Date();
        order.setCreatedAt(createdAt);
        assertEquals(createdAt, order.getCreatedAt());

        Date updatedAt = new Date();
        order.setUpdatedAt(updatedAt);
        assertEquals(updatedAt, order.getUpdatedAt());
    }

    @Test
    void testBasicOrderBuilder() {
        assertEquals(customerId, orderBuilder.getCustomerId());
        assertEquals(itemName, orderBuilder.getItemName());
        assertEquals(itemCondition, orderBuilder.getItemCondition());
        assertEquals(repairRequest, orderBuilder.getRepairRequest());
        assertEquals(serviceDate, orderBuilder.getServiceDate());
        assertEquals(PaymentMethod.BANK_TRANSFER.getValue(), orderBuilder.getPaymentMethod());
        assertNull(orderBuilder.getPaymentDetails());
        assertEquals(OrderStatus.PENDING.name(), orderBuilder.getStatus());
        assertFalse(orderBuilder.isUsingCoupon());
        assertNull(orderBuilder.getCouponCode());
    }

    @Test
    void testOrderWithRandomTechnician() {
        String[] technicians = {"tech1", "tech2", "tech3"};
        Order order = orderBuilder.setRandomTechnician(technicians).build();

        assertNotNull(order.getTechnicianId());
        assertTrue(order.getTechnicianId().startsWith("tech"));
        assertEquals(OrderStatus.WAITING_APPROVAL.name(), order.getStatus());
    }

    @Test
    void testRandomTechnicianWithEmptyArray() {
        String[] emptyArray = {};
        Order order = orderBuilder.setRandomTechnician(emptyArray).build();

        assertNull(order.getTechnicianId());
        assertEquals(OrderStatus.PENDING.name(), order.getStatus());
    }

    @Test
    void testRandomTechnicianWithNullArray() {
        Order order = orderBuilder.setRandomTechnician(null).build();

        assertNull(order.getTechnicianId());
        assertEquals(OrderStatus.PENDING.name(), order.getStatus());
    }

    @Test
    void testSetTechnicianId() {
        Order order = orderBuilder.setTechnicianId("tech789").build();
        assertEquals("tech789", order.getTechnicianId());
        assertEquals(OrderStatus.WAITING_APPROVAL.name(), order.getStatus());
    }

    @Test
    void testSetEmptyTechnicianId() {
        Order order = orderBuilder.setTechnicianId("").build();
        assertNull(order.getTechnicianId());
        assertEquals(OrderStatus.PENDING.name(), order.getStatus());
    }

    @Test
    void testSetNullTechnicianId() {
        Order order = orderBuilder.setTechnicianId(null).build();
        assertNull(order.getTechnicianId());
        assertEquals(OrderStatus.PENDING.name(), order.getStatus());
    }

    @Test
    void testSetStatus() {
        Order order = orderBuilder.setStatus(OrderStatus.APPROVED).build();
        assertEquals(OrderStatus.APPROVED.name(), order.getStatus());
    }

    @Test
    void testSetCustomPaymentDetails() {
        String paymentDetails = "Bank account: 1234567890";
        Order order = orderBuilder.setCustomPaymentDetails(paymentDetails).build();
        assertEquals(paymentDetails, order.getPaymentDetails());
    }

    @Test
    void testPaymentDetailsFromPaymentMethod() {
        Order order = orderBuilder.build();
        assertEquals(PaymentMethod.BANK_TRANSFER.getValue(), order.getPaymentDetails());
    }

    @Test
    void testSetCouponCodeSetsUsingCoupon() {
        Order order = orderBuilder.setCouponCode("COUPON123").build();
        assertTrue(order.isUsingCoupon());
        assertEquals("COUPON123", order.getCouponCode());
    }

    @Test
    void testSetEmptyCouponCode() {
        Order order = orderBuilder.setCouponCode("").build();
        assertFalse(order.isUsingCoupon());
        assertEquals("", order.getCouponCode());
    }

    @Test
    void testSetNullCouponCode() {
        Order order = orderBuilder.setCouponCode(null).build();
        assertFalse(order.isUsingCoupon());
        assertNull(order.getCouponCode());
    }

    @Test
    void testChainability() {
        assertSame(orderBuilder, orderBuilder.setStatus(OrderStatus.APPROVED));
        assertSame(orderBuilder, orderBuilder.setTechnicianId("tech123"));
        assertSame(orderBuilder, orderBuilder.setRandomTechnician(new String[]{"tech1"}));
        assertSame(orderBuilder, orderBuilder.setPaymentMethod(PaymentMethod.CASH_ON_DELIVERY));
        assertSame(orderBuilder, orderBuilder.setCustomPaymentDetails("Cash on delivery"));
        assertSame(orderBuilder, orderBuilder.setUsingCoupon(true));
        assertSame(orderBuilder, orderBuilder.setCouponCode("COUPON123"));
    }

    @Test
    void testOrderStatusEnums() {
        assertEquals(7, OrderStatus.values().length);

        assertNotNull(OrderStatus.PENDING);
        assertNotNull(OrderStatus.WAITING_APPROVAL);
        assertNotNull(OrderStatus.APPROVED);
        assertNotNull(OrderStatus.IN_PROGRESS);
        assertNotNull(OrderStatus.COMPLETED);
        assertNotNull(OrderStatus.REJECTED);
        assertNotNull(OrderStatus.CANCELLED);
    }

    @Test
    void testOrderStatusEnumValues() {
        assertEquals("PENDING", OrderStatus.PENDING.getValue());
        assertEquals("WAITING_APPROVAL", OrderStatus.WAITING_APPROVAL.getValue());
        assertEquals("APPROVED", OrderStatus.APPROVED.getValue());
        assertEquals("IN_PROGRESS", OrderStatus.IN_PROGRESS.getValue());
        assertEquals("COMPLETED", OrderStatus.COMPLETED.getValue());
        assertEquals("REJECTED", OrderStatus.REJECTED.getValue());
        assertEquals("CANCELLED", OrderStatus.CANCELLED.getValue());
    }

    @Test
    void testPaymentMethodEnums() {
        assertEquals(3, PaymentMethod.values().length);

        assertNotNull(PaymentMethod.BANK_TRANSFER);
        assertNotNull(PaymentMethod.E_WALLET);
        assertNotNull(PaymentMethod.CASH_ON_DELIVERY);
    }

    @Test
    void testPaymentMethodEnumValues() {
        assertEquals("BANK_TRANSFER", PaymentMethod.BANK_TRANSFER.getValue());
        assertEquals("E_WALLET", PaymentMethod.E_WALLET.getValue());
        assertEquals("CASH_ON_DELIVERY", PaymentMethod.CASH_ON_DELIVERY.getValue());
    }

    @Test
    void testOrderStatusContains() {
        assertTrue(OrderStatus.contains(OrderStatus.PENDING.name()));
        assertTrue(OrderStatus.contains(OrderStatus.APPROVED.name()));
        assertFalse(OrderStatus.contains("INVALID_STATUS"));
    }

    @Test
    void testPaymentMethodContains() {
        assertTrue(PaymentMethod.contains(PaymentMethod.BANK_TRANSFER.getValue()));
        assertTrue(PaymentMethod.contains(PaymentMethod.E_WALLET.getValue()));
        assertFalse(PaymentMethod.contains("BITCOIN"));
    }

    @Test
    void testPaymentMethodFromValue() {
        assertEquals(PaymentMethod.BANK_TRANSFER, PaymentMethod.fromValue("BANK_TRANSFER"));
        assertEquals(PaymentMethod.E_WALLET, PaymentMethod.fromValue("E_WALLET"));
        assertEquals(PaymentMethod.CASH_ON_DELIVERY, PaymentMethod.fromValue("CASH_ON_DELIVERY"));
        assertThrows(IllegalArgumentException.class, () -> PaymentMethod.fromValue("INVALID_METHOD"));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "PENDING", "WAITING_APPROVAL", "APPROVED",
            "IN_PROGRESS", "COMPLETED", "REJECTED", "CANCELLED"
    })
    void testOrderStatusContainsWithValidStatus(String status) {
        assertTrue(OrderStatus.contains(status));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "INVALID", "pending", "Pending", "PROCESSING", "", " PENDING "
    })
    void testOrderStatusContainsWithInvalidStatus(String status) {
        assertFalse(OrderStatus.contains(status));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "BANK_TRANSFER", "E_WALLET", "CASH_ON_DELIVERY"
    })
    void testPaymentMethodContainsWithValidMethod(String method) {
        assertTrue(PaymentMethod.contains(method));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "INVALID", "bank_transfer", "Bank Transfer", "CREDIT_CARD", "", " BANK_TRANSFER "
    })
    void testPaymentMethodContainsWithInvalidMethod(String method) {
        assertFalse(PaymentMethod.contains(method));
    }
}