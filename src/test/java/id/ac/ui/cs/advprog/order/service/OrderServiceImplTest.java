package id.ac.ui.cs.advprog.order.service;

import id.ac.ui.cs.advprog.order.dto.OrderRequestDTO;
import id.ac.ui.cs.advprog.order.dto.OrderResponseDTO;
import id.ac.ui.cs.advprog.order.dto.OrderListResponseDTO;
import id.ac.ui.cs.advprog.order.dto.UpdateOrderRequestDTO;
import id.ac.ui.cs.advprog.order.dto.ResponseDTO;
import id.ac.ui.cs.advprog.order.enums.OrderStatus;
import id.ac.ui.cs.advprog.order.model.Order;
import id.ac.ui.cs.advprog.order.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private UUID orderId;
    private UUID customerId;
    private UUID technicianId;
    private Date serviceDate;
    private Order pendingOrder;
    private Order approvedOrder;
    private OrderRequestDTO orderRequestDTO;
    private UpdateOrderRequestDTO updateOrderRequestDTO;
    private UUID bankTransferPaymentId;
    private UUID creditCardPaymentId;

    @BeforeEach
    void setUp() {
        orderId = UUID.randomUUID();
        customerId = UUID.randomUUID();
        technicianId = UUID.randomUUID();
        serviceDate = new Date();
        LocalDateTime now = LocalDateTime.now();
        bankTransferPaymentId = UUID.fromString("0000a456-4002-4567-e89b-0000000012d3");
        creditCardPaymentId = UUID.fromString("123e4567-e89b-12d3-a456-426614174002");

        pendingOrder = new Order();
        pendingOrder.setId(orderId);
        pendingOrder.setCustomerId(customerId);
        pendingOrder.setTechnicianId(technicianId);
        pendingOrder.setItemName("Dell XPS 15 9500");
        pendingOrder.setItemCondition("OLED display cracked with visible impact marks at bottom-right corner");
        pendingOrder.setRepairDetails("Matrix failure on 40% of screen area caused by physical impact - requires full panel replacement");
        pendingOrder.setServiceDate(serviceDate);
        pendingOrder.setStatus(OrderStatus.PENDING);
        pendingOrder.setCreatedAt(now);
        pendingOrder.setUpdatedAt(now);
        pendingOrder.setPaymentMethodId(bankTransferPaymentId);

        approvedOrder = new Order();
        approvedOrder.setId(UUID.randomUUID());
        approvedOrder.setCustomerId(customerId);
        approvedOrder.setTechnicianId(technicianId);
        approvedOrder.setItemName("iPhone 14 Pro Max");
        approvedOrder.setItemCondition("Severe liquid damage with corrosion on logic board");
        approvedOrder.setRepairDetails("Device boots to Apple logo then shuts down - diagnosed with short circuit in power management IC");
        approvedOrder.setServiceDate(serviceDate);
        approvedOrder.setStatus(OrderStatus.APPROVED);
        approvedOrder.setCreatedAt(now);
        approvedOrder.setUpdatedAt(now);
        approvedOrder.setPaymentMethodId(creditCardPaymentId);

        orderRequestDTO = new OrderRequestDTO();
        orderRequestDTO.setCustomerId(customerId);
        orderRequestDTO.setTechnicianId(technicianId);
        orderRequestDTO.setItemName("Lenovo ThinkPad P1 Gen 5");
        orderRequestDTO.setItemCondition("Faulty keyboard with multiple unresponsive keys (E, R, T)");
        orderRequestDTO.setRepairDetails("Mechanical failure in keyboard flex cable due to repeated stress - requires full keyboard assembly replacement");
        orderRequestDTO.setServiceDate(serviceDate);
        orderRequestDTO.setPaymentMethodId(bankTransferPaymentId);

        updateOrderRequestDTO = new UpdateOrderRequestDTO();
        updateOrderRequestDTO.setItemName("MacBook Pro M2 2023");
        updateOrderRequestDTO.setItemCondition("Liquid damage on both keyboard and trackpad");
        updateOrderRequestDTO.setRepairDetails("Corrosion detected in keyboard backlight circuit and trackpad flex connector - full disassembly required for cleaning and component replacement");
        updateOrderRequestDTO.setServiceDate(serviceDate);
        updateOrderRequestDTO.setTechnicianId(UUID.randomUUID());
        updateOrderRequestDTO.setPaymentMethodId(creditCardPaymentId);
    }

    @Test
    void testCreateOrder_Success() {
        when(orderRepository.save(any(Order.class))).thenReturn(pendingOrder);

        OrderResponseDTO response = orderService.createOrder(orderRequestDTO);

        assertNotNull(response);
        assertEquals(orderId, response.getId());
        assertEquals(customerId, response.getCustomerId());
        assertEquals(technicianId, response.getTechnicianId());
        assertEquals("Dell XPS 15 9500", response.getItemName());
        assertEquals("OLED display cracked with visible impact marks at bottom-right corner", response.getItemCondition());
        assertEquals("Matrix failure on 40% of screen area caused by physical impact - requires full panel replacement", response.getRepairDetails());
        assertEquals(serviceDate, response.getServiceDate());
        assertEquals(OrderStatus.PENDING, response.getStatus());
        assertEquals(bankTransferPaymentId, response.getPaymentMethodId());

        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testGetOrderById_Success() {
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(pendingOrder));

        OrderResponseDTO response = orderService.getOrderById(orderId);

        assertNotNull(response);
        assertEquals(orderId, response.getId());
        assertEquals(customerId, response.getCustomerId());
        assertEquals("Dell XPS 15 9500", response.getItemName());
        assertEquals(OrderStatus.PENDING, response.getStatus());

        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void testGetOrderById_NotFound() {
        UUID nonExistentId = UUID.randomUUID();
        when(orderRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.getOrderById(nonExistentId));
        verify(orderRepository, times(1)).findById(nonExistentId);
    }

    @Test
    void testGetOrdersByCustomerId_Success() {
        List<Order> orders = Arrays.asList(pendingOrder, approvedOrder);
        when(orderRepository.findByCustomerId(customerId)).thenReturn(orders);

        OrderListResponseDTO response = orderService.getOrdersByCustomerId(customerId);

        assertNotNull(response);
        assertEquals(2, response.getCount());
        assertEquals(2, response.getOrders().size());
        assertEquals(orderId, response.getOrders().get(0).getId());
        assertEquals("Dell XPS 15 9500", response.getOrders().get(0).getItemName());
        assertEquals("iPhone 14 Pro Max", response.getOrders().get(1).getItemName());

        verify(orderRepository, times(1)).findByCustomerId(customerId);
    }

    @Test
    void testUpdateOrder_Success() {
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(pendingOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(pendingOrder);

        OrderResponseDTO response = orderService.updateOrder(orderId, updateOrderRequestDTO);

        assertNotNull(response);
        assertEquals(orderId, response.getId());
        assertEquals("MacBook Pro M2 2023", response.getItemName());
        assertEquals("Liquid damage on both keyboard and trackpad", response.getItemCondition());
        assertEquals("Corrosion detected in keyboard backlight circuit and trackpad flex connector - full disassembly required for cleaning and component replacement", response.getRepairDetails());
        assertEquals(updateOrderRequestDTO.getTechnicianId(), response.getTechnicianId());
        assertEquals(bankTransferPaymentId, response.getPaymentMethodId());

        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testUpdateOrder_NotFound() {
        UUID nonExistentId = UUID.randomUUID();
        when(orderRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.updateOrder(nonExistentId, updateOrderRequestDTO));
        verify(orderRepository, times(1)).findById(nonExistentId);
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void testUpdateOrder_AlreadyApproved() {
        UUID approvedOrderId = approvedOrder.getId();
        when(orderRepository.findById(approvedOrderId)).thenReturn(Optional.of(approvedOrder));

        assertThrows(IllegalStateException.class, () -> orderService.updateOrder(approvedOrderId, updateOrderRequestDTO));
        verify(orderRepository, times(1)).findById(approvedOrderId);
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void testCancelOrder_Success() {
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(pendingOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(pendingOrder);

        ResponseDTO response = orderService.cancelOrder(orderId);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("Order has been successfully cancelled", response.getMessage());
        assertEquals(OrderStatus.CANCELLED, pendingOrder.getStatus());

        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, times(1)).save(pendingOrder);
    }

    @Test
    void testCancelOrder_NotFound() {
        UUID nonExistentId = UUID.randomUUID();
        when(orderRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.cancelOrder(nonExistentId));
        verify(orderRepository, times(1)).findById(nonExistentId);
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void testCancelOrder_AlreadyApproved() {
        UUID approvedOrderId = approvedOrder.getId();
        when(orderRepository.findById(approvedOrderId)).thenReturn(Optional.of(approvedOrder));

        assertThrows(IllegalStateException.class, () -> orderService.cancelOrder(approvedOrderId));
        verify(orderRepository, times(1)).findById(approvedOrderId);
        verify(orderRepository, never()).save(any(Order.class));
    }
}