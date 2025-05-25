package id.ac.ui.cs.advprog.order.service;

import id.ac.ui.cs.advprog.order.dto.OrderListResponseDTO;
import id.ac.ui.cs.advprog.order.dto.OrderRequestDTO;
import id.ac.ui.cs.advprog.order.dto.OrderResponseDTO;
import id.ac.ui.cs.advprog.order.dto.ResponseDTO;
import id.ac.ui.cs.advprog.order.dto.UpdateOrderRequestDTO;
import id.ac.ui.cs.advprog.order.enums.OrderStatus;
import id.ac.ui.cs.advprog.order.model.Order;
import id.ac.ui.cs.advprog.order.model.OrderBuilder;
import id.ac.ui.cs.advprog.order.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository orderRepository;

    private OrderRequestDTO requestDto;
    private UpdateOrderRequestDTO updateDto;
    private OrderBuilder orderBuilder;
    private UUID customerId;
    private UUID couponId;
    private Date serviceDate;

    @BeforeEach
    void setUp() {
        customerId = UUID.randomUUID();
        UUID paymentMethodId = UUID.randomUUID();
        couponId = UUID.randomUUID();
        serviceDate = new Date();

        this.orderBuilder = new OrderBuilder()
                .setCustomerId(customerId)
                .setItemName("MacBook Pro 16-inch M2")
                .setItemCondition("Brand new, still in warranty")
                .setRepairDetails("Battery drains rapidly and trackpad is unresponsive")
                .setServiceDate(serviceDate)
                .setPaymentMethodId(paymentMethodId)
                .setCouponId(couponId);

        this.requestDto = new OrderRequestDTO(
                customerId,
                null,
                "MacBook Pro 16-inch M2",
                "Brand new, still in warranty",
                "Battery drains rapidly and trackpad is unresponsive",
                serviceDate,
                paymentMethodId,
                couponId
        );

        this.updateDto = UpdateOrderRequestDTO.builder()
                .itemName("Dell XPS 15 9520")
                .itemCondition("Used for 8 months, good condition overall")
                .build();
    }

    @Test
    void testCreateOrder() throws Exception {
        Order order = orderBuilder.build();
        Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenReturn(order);

        CompletableFuture<OrderResponseDTO> future = orderService.createOrder(requestDto);
        OrderResponseDTO dto = future.get();

        assertNotNull(dto);
        assertEquals(order.getId(), dto.getId());
        assertEquals(order.getItemName(), dto.getItemName());
        Mockito.verify(orderRepository).save(Mockito.any(Order.class));
    }

    @Test
    void testGetOrderById_Success() throws Exception {
        Order order = orderBuilder.build();
        UUID id = order.getId();
        Mockito.when(orderRepository.findById(id)).thenReturn(Optional.of(order));

        CompletableFuture<OrderResponseDTO> future = orderService.getOrderById(id);
        OrderResponseDTO dto = future.get();

        assertEquals(id, dto.getId());
        assertEquals(order.getCustomerId(), dto.getCustomerId());
    }

    @Test
    void testGetOrderById_NotFound() {
        UUID nonExistentOrderId = UUID.randomUUID();
        Mockito.when(orderRepository.findById(nonExistentOrderId)).thenReturn(Optional.empty());

        CompletionException ex = assertThrows(CompletionException.class, () ->
                orderService.getOrderById(nonExistentOrderId).join()
        );
        assertTrue(ex.getCause() instanceof EntityNotFoundException);
    }

    @Test
    void testGetOrdersByCustomerId() throws Exception {
        Order order1 = orderBuilder.build();
        Order order2 = orderBuilder.setItemName("iPhone 14 Pro Max").build();
        Mockito.when(orderRepository.findByCustomerId(customerId))
                .thenReturn(List.of(order1, order2));

        CompletableFuture<OrderListResponseDTO> future = orderService.getOrdersByCustomerId(customerId);
        OrderListResponseDTO listDto = future.get();

        assertEquals(2, listDto.getCount());
        assertThat(listDto.getOrders()).extracting(OrderResponseDTO::getId)
                .containsExactly(order1.getId(), order2.getId());
    }

    @Test
    void testGetAll() throws Exception {
        Order order1 = orderBuilder.build();
        Order order2 = orderBuilder.setItemName("Samsung Galaxy S23 Ultra").build();
        Mockito.when(orderRepository.findAll()).thenReturn(List.of(order1, order2));

        CompletableFuture<OrderListResponseDTO> future = orderService.getAllOrders();
        OrderListResponseDTO dto = future.get();

        assertEquals(2, dto.getOrders().size());
        assertThat(dto.getOrders())
                .extracting(OrderResponseDTO::getItemName)
                .containsExactly(order1.getItemName(), order2.getItemName());
    }

    @Test
    void testUpdateOrder_Success() throws Exception {
        Order existingOrder = orderBuilder.build();
        UUID orderId = existingOrder.getId();
        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        Mockito.when(orderRepository.save(existingOrder)).thenReturn(existingOrder);

        CompletableFuture<OrderResponseDTO> future = orderService.updateOrder(orderId, updateDto);
        OrderResponseDTO dto = future.get();

        assertEquals("Dell XPS 15 9520", dto.getItemName());
        assertEquals("Used for 8 months, good condition overall", dto.getItemCondition());
    }

    @Test
    void testUpdateOrder_PreservesOtherFields() throws Exception {
        Order existingOrder = orderBuilder.build();
        UUID orderId = existingOrder.getId();
        String originalRepairDetails = existingOrder.getRepairDetails();
        UUID originalCouponId = existingOrder.getCouponId();
        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        Mockito.when(orderRepository.save(existingOrder)).thenReturn(existingOrder);

        CompletableFuture<OrderResponseDTO> future = orderService.updateOrder(orderId, updateDto);
        OrderResponseDTO dto = future.get();

        assertEquals("Dell XPS 15 9520", dto.getItemName());
        assertEquals("Used for 8 months, good condition overall", dto.getItemCondition());
        assertEquals(originalRepairDetails, dto.getRepairDetails());
        assertEquals(originalCouponId, dto.getCouponId());
    }

    @Test
    void testUpdateOrder_NotFound() {
        UUID nonExistentOrderId = UUID.randomUUID();
        Mockito.when(orderRepository.findById(nonExistentOrderId)).thenReturn(Optional.empty());

        CompletionException ex = assertThrows(CompletionException.class, () ->
                orderService.updateOrder(nonExistentOrderId, updateDto).join()
        );
        assertTrue(ex.getCause() instanceof EntityNotFoundException);
    }

    @Test
    void testCancelOrder_Success() throws Exception {
        Order existingOrder = orderBuilder.build();
        UUID orderId = existingOrder.getId();
        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        Mockito.when(orderRepository.save(existingOrder)).thenReturn(existingOrder);

        CompletableFuture<ResponseDTO> future = orderService.cancelOrder(orderId);
        ResponseDTO response = future.get();

        assertTrue(response.isSuccess());
        assertEquals("Order cancelled: " + orderId, response.getMessage());
        assertEquals(OrderStatus.CANCELLED, existingOrder.getStatus());
    }

    @Test
    void testCancelOrder_NotFound() {
        UUID nonExistentOrderId = UUID.randomUUID();
        Mockito.when(orderRepository.findById(nonExistentOrderId)).thenReturn(Optional.empty());

        CompletionException ex = assertThrows(CompletionException.class, () ->
                orderService.cancelOrder(nonExistentOrderId).join()
        );
        assertTrue(ex.getCause() instanceof EntityNotFoundException);
    }

    @Test
    void testUpdateOrder_AllFieldsUpdate() throws Exception {
        Order existingOrder = orderBuilder.build();
        UUID orderId = existingOrder.getId();
        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));

        UUID newTechnicianId = UUID.randomUUID();
        UUID newPaymentMethodId = UUID.randomUUID();
        UUID newCouponId = UUID.randomUUID();
        Date newServiceDate = new Date(123456789L);
        String newEstimatedCompletionTime = "2025-02-15T14:30:00";
        BigDecimal newEstimatedPrice = BigDecimal.valueOf(275.50);
        BigDecimal newFinalPrice = BigDecimal.valueOf(320.75);
        String newRepairDetails = "Complete logic board replacement and SSD upgrade to 1TB";

        UpdateOrderRequestDTO completeUpdateRequest = UpdateOrderRequestDTO.builder()
                .itemName("ASUS ROG Strix Gaming Laptop")
                .itemCondition("Refurbished with 6-month warranty")
                .repairDetails(newRepairDetails)
                .serviceDate(newServiceDate)
                .technicianId(newTechnicianId)
                .paymentMethodId(newPaymentMethodId)
                .couponId(newCouponId)
                .estimatedCompletionTime(newEstimatedCompletionTime)
                .estimatedPrice(newEstimatedPrice)
                .finalPrice(newFinalPrice)
                .build();

        Mockito.when(orderRepository.save(Mockito.any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        OrderResponseDTO resultDto = orderService.updateOrder(orderId, completeUpdateRequest).get();

        assertEquals("ASUS ROG Strix Gaming Laptop", resultDto.getItemName());
        assertEquals("Refurbished with 6-month warranty", resultDto.getItemCondition());
        assertEquals(newRepairDetails, resultDto.getRepairDetails());
        assertEquals(newServiceDate, resultDto.getServiceDate());
        assertEquals(newTechnicianId, resultDto.getTechnicianId());
        assertEquals(newPaymentMethodId, resultDto.getPaymentMethodId());
        assertEquals(newCouponId, resultDto.getCouponId());
        assertEquals(newEstimatedCompletionTime, resultDto.getEstimatedCompletionTime());
        assertEquals(newEstimatedPrice, resultDto.getEstimatedPrice());
        assertEquals(newFinalPrice, resultDto.getFinalPrice());
    }
}
