package id.ac.ui.cs.advprog.order.controller;

import id.ac.ui.cs.advprog.order.dto.*;
import id.ac.ui.cs.advprog.order.enums.OrderStatus;
import id.ac.ui.cs.advprog.order.enums.PaymentMethod;
import id.ac.ui.cs.advprog.order.model.Order;
import id.ac.ui.cs.advprog.order.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private UUID testOrderId;
    private UUID testCustomerId;
    private UUID testTechnicianId;
    private CreateOrderRequest validCreateRequest;
    private Order mockOrder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testOrderId = UUID.randomUUID();
        testCustomerId = UUID.randomUUID();
        testTechnicianId = UUID.randomUUID();

        validCreateRequest = new CreateOrderRequest();
        validCreateRequest.setCustomerId(testCustomerId);
        validCreateRequest.setItemName("Broken TV");
        validCreateRequest.setItemCondition("Screen not turning on");
        validCreateRequest.setRepairRequest("Fix the display");
        validCreateRequest.setServiceDate(new Date());
        validCreateRequest.setPaymentMethod(PaymentMethod.BANK_TRANSFER.getValue());
        validCreateRequest.setUsingCoupon(false);

        mockOrder = new Order();
        mockOrder.setId(testOrderId);
        mockOrder.setCustomerId(testCustomerId);
        mockOrder.setItemName("Broken TV");
        mockOrder.setStatus(OrderStatus.PENDING.name());
    }

    @Test
    void testCreateOrder_Success() {
        when(orderService.createOrder(any(CreateOrderRequest.class))).thenReturn(mockOrder);

        ResponseEntity<Order> response = orderController.createOrder(validCreateRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockOrder, response.getBody());
        verify(orderService, times(1)).createOrder(validCreateRequest);
    }

    @Test
    void testGetAllOrders_Success() {
        List<Order> mockOrders = Arrays.asList(mockOrder);
        when(orderService.getAllOrders()).thenReturn(mockOrders);

        ResponseEntity<List<Order>> response = orderController.getAllOrders();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockOrders, response.getBody());
        verify(orderService, times(1)).getAllOrders();
    }

    @Test
    void testGetOrderById_Success() {
        when(orderService.getOrderById(testOrderId)).thenReturn(mockOrder);

        ResponseEntity<Order> response = orderController.getOrderById(testOrderId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockOrder, response.getBody());
        verify(orderService, times(1)).getOrderById(testOrderId);
    }

    @Test
    void testGetOrdersByCustomerId_Success() {
        List<Order> mockOrders = Arrays.asList(mockOrder);
        when(orderService.getOrdersByCustomerId(testCustomerId)).thenReturn(mockOrders);

        ResponseEntity<List<Order>> response = orderController.getOrdersByCustomerId(testCustomerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockOrders, response.getBody());
        verify(orderService, times(1)).getOrdersByCustomerId(testCustomerId);
    }

    @Test
    void testGetOrdersByTechnicianId_Success() {
        List<Order> mockOrders = Arrays.asList(mockOrder);
        when(orderService.getOrdersByTechnicianId(testTechnicianId)).thenReturn(mockOrders);

        ResponseEntity<List<Order>> response = orderController.getOrdersByTechnicianId(testTechnicianId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockOrders, response.getBody());
        verify(orderService, times(1)).getOrdersByTechnicianId(testTechnicianId);
    }

    @Test
    void testUpdateOrder_Success() {
        UpdateOrderRequest updateRequest = new UpdateOrderRequest();
        updateRequest.setItemName("Updated TV");
        updateRequest.setItemCondition("Updated condition");
        updateRequest.setRepairRequest("Updated request");
        updateRequest.setServiceDate(new Date());

        mockOrder.setItemName("Updated TV");
        when(orderService.updateOrder(eq(testOrderId), any(UpdateOrderRequest.class))).thenReturn(mockOrder);

        ResponseEntity<Order> response = orderController.updateOrder(testOrderId, updateRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockOrder, response.getBody());
        verify(orderService, times(1)).updateOrder(testOrderId, updateRequest);
    }

    @Test
    void testAssignTechnician_Success() {
        mockOrder.setTechnicianId(testTechnicianId);
        mockOrder.setStatus(OrderStatus.WAITING_APPROVAL.name());

        when(orderService.assignTechnician(testOrderId, testTechnicianId)).thenReturn(mockOrder);

        ResponseEntity<Order> response = orderController.assignTechnician(testOrderId, testTechnicianId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockOrder, response.getBody());
        verify(orderService, times(1)).assignTechnician(testOrderId, testTechnicianId);
    }

    @Test
    void testCancelOrder_Success() {
        mockOrder.setStatus(OrderStatus.CANCELLED.name());
        when(orderService.cancelOrder(testOrderId)).thenReturn(mockOrder);

        ResponseEntity<Order> response = orderController.cancelOrder(testOrderId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockOrder, response.getBody());
        verify(orderService, times(1)).cancelOrder(testOrderId);
    }

    @Test
    void testProvideEstimate_Success() {
        RepairEstimateRequest estimateRequest = new RepairEstimateRequest();
        estimateRequest.setRepairEstimate("2 days");
        estimateRequest.setRepairPrice(150.0);

        mockOrder.setRepairEstimate("2 days");
        mockOrder.setRepairPrice(150.0);

        when(orderService.provideEstimate(eq(testOrderId), any(RepairEstimateRequest.class))).thenReturn(mockOrder);

        ResponseEntity<Order> response = orderController.provideEstimate(testOrderId, estimateRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockOrder, response.getBody());
        verify(orderService, times(1)).provideEstimate(testOrderId, estimateRequest);
    }

    @Test
    void testApproveOrder_Success() {
        mockOrder.setStatus(OrderStatus.APPROVED.name());
        when(orderService.approveOrder(testOrderId)).thenReturn(mockOrder);

        ResponseEntity<Order> response = orderController.approveOrder(testOrderId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockOrder, response.getBody());
        verify(orderService, times(1)).approveOrder(testOrderId);
    }

    @Test
    void testRejectOrder_Success() {
        mockOrder.setStatus(OrderStatus.REJECTED.name());
        when(orderService.rejectOrder(testOrderId)).thenReturn(mockOrder);

        ResponseEntity<Order> response = orderController.rejectOrder(testOrderId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockOrder, response.getBody());
        verify(orderService, times(1)).rejectOrder(testOrderId);
    }

    @Test
    void testStartProgress_Success() {
        mockOrder.setStatus(OrderStatus.IN_PROGRESS.name());
        when(orderService.startProgress(testOrderId)).thenReturn(mockOrder);

        ResponseEntity<Order> response = orderController.startProgress(testOrderId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockOrder, response.getBody());
        verify(orderService, times(1)).startProgress(testOrderId);
    }

    @Test
    void testCompleteOrder_Success() {
        mockOrder.setStatus(OrderStatus.COMPLETED.name());
        when(orderService.completeOrder(testOrderId)).thenReturn(mockOrder);

        ResponseEntity<Order> response = orderController.completeOrder(testOrderId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockOrder, response.getBody());
        verify(orderService, times(1)).completeOrder(testOrderId);
    }

    @Test
    void testSubmitRepairReport_Success() {
        RepairReportRequest reportRequest = new RepairReportRequest();
        reportRequest.setRepairReport("Fixed the display by replacing the LCD panel");

        mockOrder.setRepairReport("Fixed the display by replacing the LCD panel");

        when(orderService.submitRepairReport(eq(testOrderId), any(RepairReportRequest.class))).thenReturn(mockOrder);

        ResponseEntity<Order> response = orderController.submitRepairReport(testOrderId, reportRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockOrder, response.getBody());
        verify(orderService, times(1)).submitRepairReport(testOrderId, reportRequest);
    }
}