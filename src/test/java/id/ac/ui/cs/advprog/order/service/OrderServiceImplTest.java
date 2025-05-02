package id.ac.ui.cs.advprog.order.service;

import id.ac.ui.cs.advprog.order.enums.OrderStatus;
import id.ac.ui.cs.advprog.order.model.Order;
import id.ac.ui.cs.advprog.order.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Order testOrder;
    private UUID testOrderId;
    private UUID testCustomerId;
    private UUID testTechnicianId;

    @BeforeEach
    void setUp() {
        testOrderId = UUID.randomUUID();
        testCustomerId = UUID.randomUUID();
        testTechnicianId = UUID.randomUUID();

        testOrder = new Order();
        testOrder.setId(testOrderId);
        testOrder.setCustomerId(testCustomerId);
        testOrder.setTechnicianId(testTechnicianId);
        testOrder.setStatus(OrderStatus.WAITING_APPROVAL.name());
        testOrder.setItemName("Laptop");
        testOrder.setRepairRequest("Screen replacement");
        testOrder.setPaymentMethod("E_WALLET");
    }

    @Test
    void createOrder_ShouldSaveOrderAndNotifyTechnician() {
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        Order result = orderService.createOrder(testOrder);

        verify(orderRepository).save(testOrder);
        verify(notificationService).createNotification(
                testTechnicianId,
                "New Repair Order",
                "You have a new repair order for Laptop"
        );
        assertEquals(testOrderId, result.getId());
    }

    @Test
    void getCustomerOrders_ShouldReturnOrders() {
        List<Order> expected = Collections.singletonList(testOrder);
        when(orderRepository.findByCustomerId(testCustomerId)).thenReturn(expected);

        List<Order> result = orderService.getCustomerOrders(testCustomerId);

        assertEquals(1, result.size());
        assertEquals(testOrderId, result.get(0).getId());
    }

    @Test
    void findById_WhenOrderExists_ShouldReturnOrder() {
        when(orderRepository.findById(testOrderId)).thenReturn(Optional.of(testOrder));

        Order result = orderService.findById(testOrderId);

        assertEquals(testOrderId, result.getId());
    }

    @Test
    void findById_WhenOrderNotExists_ShouldReturnNull() {
        when(orderRepository.findById(testOrderId)).thenReturn(Optional.empty());

        Order result = orderService.findById(testOrderId);

        assertNull(result);
    }

    @Test
    void updateStatus_ShouldChangeStatus() {
        String newStatus = OrderStatus.APPROVED.name();
        when(orderRepository.findById(testOrderId)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        Order result = orderService.updateStatus(testOrderId, newStatus);

        assertEquals(newStatus, result.getStatus());
        verify(orderRepository).save(testOrder);
    }

    @Test
    void updateStatus_WhenOrderNotFound_ShouldThrow() {
        when(orderRepository.findById(testOrderId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                orderService.updateStatus(testOrderId, "ANY_STATUS")
        );
    }

    @Test
    void updateOrderDetails_WhenValid_ShouldUpdate() throws Exception {
        Order updatedOrder = new Order();
        updatedOrder.setItemName("Updated Laptop");
        updatedOrder.setPaymentMethod("E_WALLET");

        when(orderRepository.findById(testOrderId)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        Order result = orderService.updateOrderDetails(testOrderId, updatedOrder);

        assertEquals("Updated Laptop", result.getItemName());
        verify(orderRepository).save(testOrder);
    }

    @Test
    void updateOrderDetails_WhenInvalidStatus_ShouldThrow() {
        testOrder.setStatus(OrderStatus.COMPLETED.name());
        when(orderRepository.findById(testOrderId)).thenReturn(Optional.of(testOrder));

        assertThrows(IllegalStateException.class, () ->
                orderService.updateOrderDetails(testOrderId, new Order())
        );
    }

    @Test
    void changeTechnician_ShouldUpdateAndNotify() throws Exception {
        UUID newTechnicianId = UUID.randomUUID();
        when(orderRepository.findById(testOrderId)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        Order result = orderService.changeTechnician(testOrderId, newTechnicianId);

        verify(notificationService).createNotification(
                testTechnicianId,
                "Order Reassigned",
                "An order for Laptop has been reassigned."
        );
        verify(notificationService).createNotification(
                newTechnicianId,
                "New Repair Order",
                "You have been assigned a new repair order for Laptop"
        );
        assertEquals(newTechnicianId, result.getTechnicianId());
    }

    @Test
    void cancelOrder_WhenAuthorized_ShouldCancel() throws Exception {
        when(orderRepository.findById(testOrderId)).thenReturn(Optional.of(testOrder));

        orderService.cancelOrder(testOrderId, testCustomerId);

        assertEquals(OrderStatus.CANCELLED.name(), testOrder.getStatus());
        verify(notificationService).createNotification(
                testTechnicianId,
                "Order Canceled",
                "An order for Laptop has been canceled."
        );
    }

    @Test
    void cancelOrder_WhenUnauthorized_ShouldThrow() {
        when(orderRepository.findById(testOrderId)).thenReturn(Optional.of(testOrder));

        assertThrows(IllegalStateException.class, () ->
                orderService.cancelOrder(testOrderId, UUID.randomUUID())
        );
    }

    @Test
    void completeOrder_WhenAuthorized_ShouldComplete() throws Exception {
        String report = "Screen replaced successfully";
        testOrder.setStatus(OrderStatus.IN_PROGRESS.name());
        when(orderRepository.findById(testOrderId)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        Order result = orderService.completeOrder(testOrderId, testTechnicianId, report);

        assertEquals(OrderStatus.COMPLETED.name(), result.getStatus());
        assertEquals(report, result.getRepairReport());
        verify(notificationService).createNotification(
                testCustomerId,
                "Repair Completed",
                "Your Laptop has been repaired."
        );
    }

    @Test
    void completeOrder_WhenUnauthorized_ShouldThrow() {
        when(orderRepository.findById(testOrderId)).thenReturn(Optional.of(testOrder));

        assertThrows(IllegalStateException.class, () ->
                orderService.completeOrder(testOrderId, UUID.randomUUID(), "report")
        );
    }

    @Test
    void updateStatus_WithInvalidStatus_ShouldThrow() {
        when(orderRepository.findById(testOrderId)).thenReturn(Optional.of(testOrder));

        assertThrows(IllegalArgumentException.class, () ->
                orderService.updateStatus(testOrderId, "INVALID_STATUS")
        );
    }

    @Test
    void cancelOrder_WhenStatusCannotBeCancelled_ShouldThrow() {
        testOrder.setStatus(OrderStatus.COMPLETED.name());
        when(orderRepository.findById(testOrderId)).thenReturn(Optional.of(testOrder));

        assertThrows(IllegalStateException.class, () ->
                orderService.cancelOrder(testOrderId, testCustomerId)
        );
    }
}