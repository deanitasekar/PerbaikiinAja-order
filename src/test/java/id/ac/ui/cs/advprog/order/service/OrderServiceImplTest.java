package id.ac.ui.cs.advprog.order.service;

import id.ac.ui.cs.advprog.order.model.Order;
import id.ac.ui.cs.advprog.order.repository.OrderRepository;
import id.ac.ui.cs.advprog.order.enums.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Order sampleOrder;

    @BeforeEach
    void setUp() {
        sampleOrder = new Order(
                "ORD-001",
                "Laptop Rusak",
                "Mati Total",
                OrderStatus.PENDING.name(),
                "BANK_TRANSFER"
        );
    }

    @Test
    void testCreateOrder_Success() {
        doNothing().when(orderRepository).save(sampleOrder);

        Order result = orderService.createOrder(sampleOrder);

        verify(orderRepository, times(1)).save(sampleOrder);

        assertNotNull(result);
        assertEquals("ORD-001", result.getOrderId());
    }

    @Test
    void testUpdateStatus_Success() {
        when(orderRepository.findById("ORD-001")).thenReturn(sampleOrder);
        doNothing().when(orderRepository).save(sampleOrder);

        Order updated = orderService.updateStatus("ORD-001", "SUCCESS");

        assertEquals(OrderStatus.SUCCESS, updated.getStatus());

        verify(orderRepository, times(1)).save(sampleOrder);
    }

    @Test
    void testUpdateStatus_NotFound() {
        when(orderRepository.findById("ORD-999")).thenThrow(
                new NoSuchElementException("No order found")
        );

        assertThrows(
                NoSuchElementException.class,
                () -> orderService.updateStatus("ORD-999", "SUCCESS")
        );
    }

    @Test
    void testFindById_Success() {
        when(orderRepository.findById("ORD-001")).thenReturn(sampleOrder);

        Order found = orderService.findById("ORD-001");
        assertNotNull(found);
        assertEquals("Laptop Rusak", found.getItemName());
    }

    @Test
    void testFindById_NotFound() {
        when(orderRepository.findById("ORD-002"))
                .thenThrow(new NoSuchElementException("No order found"));

        assertThrows(
                NoSuchElementException.class,
                () -> orderService.findById("ORD-002")
        );
    }
}
