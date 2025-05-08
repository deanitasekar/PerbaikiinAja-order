package id.ac.ui.cs.advprog.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.order.dto.OrderListResponseDTO;
import id.ac.ui.cs.advprog.order.dto.OrderRequestDTO;
import id.ac.ui.cs.advprog.order.dto.OrderResponseDTO;
import id.ac.ui.cs.advprog.order.dto.ResponseDTO;
import id.ac.ui.cs.advprog.order.dto.UpdateOrderRequestDTO;
import id.ac.ui.cs.advprog.order.enums.OrderStatus;
import id.ac.ui.cs.advprog.order.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID customerId;
    private UUID orderId;
    private OrderRequestDTO orderRequest;
    private UpdateOrderRequestDTO updateOrderRequest;
    private OrderResponseDTO orderResponse;

    @BeforeEach
    void setUp() {
        customerId = UUID.fromString("00000123-5189-5678-1039-185016273000");
        orderId = UUID.randomUUID();

        orderRequest = new OrderRequestDTO();
        orderRequest.setCustomerId(customerId);
        orderRequest.setItemName("MacBook Pro 16-inch M2 Max 2023");
        orderRequest.setItemCondition("OLED display shattered with spiderweb cracks across 70% of surface area");
        orderRequest.setIssueDescription("Full display assembly replacement required due to impact damage from accidental drop");
        orderRequest.setDesiredServiceDate(new Date());
        orderRequest.setPaymentMethod("Bank Transfer");

        updateOrderRequest = new UpdateOrderRequestDTO();
        updateOrderRequest.setItemCondition("Additional keyboard damage detected - multiple non-responsive keys (A,S,D,F)");
        updateOrderRequest.setIssueDescription("Combined display and keyboard replacement needed - confirmed liquid damage in lower chassis");

        orderResponse = new OrderResponseDTO();
        orderResponse.setId(orderId);
        orderResponse.setCustomerId(customerId);
        orderResponse.setItemName("MacBook Pro 16-inch M2 Max 2023");
        orderResponse.setItemCondition("OLED display shattered with spiderweb cracks across 70% of surface area");
        orderResponse.setIssueDescription("Full display assembly replacement required due to impact damage from accidental drop");
        orderResponse.setStatus(OrderStatus.PENDING);
        orderResponse.setCreatedAt(LocalDateTime.now());
        orderResponse.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    @WithMockUser(username = "00000123-5189-5678-1039-185016273000", roles = {"USER"})
    void testCreateOrderSuccess() throws Exception {
        Mockito.when(orderService.createOrder(any(OrderRequestDTO.class))).thenReturn(orderResponse);

        mockMvc.perform(
                post("/orders")
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest))
        ).andExpectAll(
                status().isCreated()
        ).andDo(result -> {
            OrderResponseDTO response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    OrderResponseDTO.class
            );

            assertNotNull(response);
            assertEquals(orderId, response.getId());
            assertEquals(customerId, response.getCustomerId());
            assertEquals("MacBook Pro 16-inch M2 Max 2023", response.getItemName());
            assertTrue(response.getIssueDescription().contains("Full display assembly replacement"));
            assertEquals(OrderStatus.PENDING, response.getStatus());
        });
    }

    @Test
    @WithMockUser(username = "00000123-5189-5678-1039-185016273000", roles = {"USER"})
    void testGetOrderHistory() throws Exception {
        List<OrderResponseDTO> orders = Arrays.asList(orderResponse);
        OrderListResponseDTO orderListResponse = new OrderListResponseDTO();
        orderListResponse.setOrders(orders);
        orderListResponse.setCount(orders.size());

        Mockito.when(orderService.getOrdersByCustomerId(customerId)).thenReturn(orderListResponse);

        mockMvc.perform(
                get("/orders")
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            OrderListResponseDTO response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    OrderListResponseDTO.class
            );

            assertNotNull(response);
            assertEquals(1, response.getCount());
            assertEquals(1, response.getOrders().size());
            assertEquals(orderId, response.getOrders().get(0).getId());
            assertEquals("MacBook Pro 16-inch M2 Max 2023", response.getOrders().get(0).getItemName());
        });
    }

    @Test
    @WithMockUser(username = "00000123-5189-5678-1039-185016273000", roles = {"USER"})
    void testUpdateOrderSuccess() throws Exception {
        OrderResponseDTO updatedResponse = new OrderResponseDTO();
        updatedResponse.setId(orderId);
        updatedResponse.setCustomerId(customerId);
        updatedResponse.setItemName("MacBook Pro 16-inch M2 Max 2023");
        updatedResponse.setItemCondition("Additional keyboard damage detected - multiple non-responsive keys (A,S,D,F)");
        updatedResponse.setIssueDescription("Combined display and keyboard replacement needed - confirmed liquid damage in lower chassis");
        updatedResponse.setStatus(OrderStatus.PENDING);

        Mockito.when(orderService.getOrderById(orderId)).thenReturn(orderResponse);
        Mockito.when(orderService.updateOrder(eq(orderId), any(UpdateOrderRequestDTO.class)))
                .thenReturn(updatedResponse);

        mockMvc.perform(
                put("/orders/" + orderId)
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateOrderRequest))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            OrderResponseDTO response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    OrderResponseDTO.class
            );

            assertNotNull(response);
            assertEquals(orderId, response.getId());
            assertEquals("MacBook Pro 16-inch M2 Max 2023", response.getItemName());
            assertEquals("Additional keyboard damage detected - multiple non-responsive keys (A,S,D,F)", response.getItemCondition());
            assertTrue(response.getIssueDescription().contains("Combined display and keyboard replacement"));
        });
    }

    @Test
    @WithMockUser(username = "00000123-5189-5678-1039-185016273000", roles = {"USER"})
    void testUpdateOrderNotFound() throws Exception {
        Mockito.when(orderService.getOrderById(orderId))
                .thenThrow(new EntityNotFoundException("Order not found"));

        mockMvc.perform(
                put("/orders/" + orderId)
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateOrderRequest))
        ).andExpectAll(
                status().isNotFound()
        );
    }

    @Test
    @WithMockUser(username = "00000123-5189-5678-1039-185016273000", roles = {"USER"})
    void testUpdateOrderForbidden() throws Exception {
        OrderResponseDTO differentOwnerResponse = new OrderResponseDTO();
        differentOwnerResponse.setId(orderId);
        differentOwnerResponse.setCustomerId(UUID.randomUUID());

        Mockito.when(orderService.getOrderById(orderId)).thenReturn(differentOwnerResponse);

        mockMvc.perform(
                put("/orders/" + orderId)
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateOrderRequest))
        ).andExpectAll(
                status().isForbidden()
        );
    }

    @Test
    @WithMockUser(username = "00000123-5189-5678-1039-185016273000", roles = {"USER"})
    void testUpdateOrderAlreadyProcessed() throws Exception {
        Mockito.when(orderService.getOrderById(orderId)).thenReturn(orderResponse);
        Mockito.when(orderService.updateOrder(eq(orderId), any(UpdateOrderRequestDTO.class)))
                .thenThrow(new IllegalStateException("Order cannot be updated after technician has reviewed it"));

        mockMvc.perform(
                put("/orders/" + orderId)
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateOrderRequest))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            ResponseDTO response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    ResponseDTO.class
            );

            assertNotNull(response);
            assertFalse(response.isSuccess());
            assertEquals("Order cannot be updated after technician has reviewed it", response.getMessage());
        });
    }

    @Test
    @WithMockUser(username = "00000123-5189-5678-1039-185016273000", roles = {"USER"})
    void testCancelOrderSuccess() throws Exception {
        ResponseDTO successResponse = new ResponseDTO();
        successResponse.setSuccess(true);
        successResponse.setMessage("Order has been successfully cancelled");

        Mockito.when(orderService.getOrderById(orderId)).thenReturn(orderResponse);
        Mockito.when(orderService.cancelOrder(orderId)).thenReturn(successResponse);

        mockMvc.perform(
                delete("/orders/" + orderId)
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            ResponseDTO response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    ResponseDTO.class
            );

            assertNotNull(response);
            assertTrue(response.isSuccess());
            assertEquals("Order has been successfully cancelled", response.getMessage());
        });
    }

    @Test
    @WithMockUser(username = "00000123-5189-5678-1039-185016273000", roles = {"USER"})
    void testCancelOrderNotFound() throws Exception {
        Mockito.when(orderService.getOrderById(orderId))
                .thenThrow(new EntityNotFoundException("Order not found"));

        mockMvc.perform(
                delete("/orders/" + orderId)
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            ResponseDTO response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    ResponseDTO.class
            );

            assertNotNull(response);
            assertFalse(response.isSuccess());
            assertEquals("Order not found", response.getMessage());
        });
    }

    @Test
    @WithMockUser(username = "00000123-5189-5678-1039-185016273000", roles = {"USER"})
    void testCancelOrderForbidden() throws Exception {
        OrderResponseDTO differentOwnerResponse = new OrderResponseDTO();
        differentOwnerResponse.setId(orderId);
        differentOwnerResponse.setCustomerId(UUID.randomUUID());

        Mockito.when(orderService.getOrderById(orderId)).thenReturn(differentOwnerResponse);

        mockMvc.perform(
                delete("/orders/" + orderId)
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isForbidden()
        );
    }

    @Test
    @WithMockUser(username = "00000123-5189-5678-1039-185016273000", roles = {"USER"})
    void testCancelOrderAlreadyProcessed() throws Exception {
        Mockito.when(orderService.getOrderById(orderId)).thenReturn(orderResponse);
        Mockito.when(orderService.cancelOrder(orderId))
                .thenThrow(new IllegalStateException("Order cannot be cancelled after technician has approved it"));

        mockMvc.perform(
                delete("/orders/" + orderId)
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            ResponseDTO response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    ResponseDTO.class
            );

            assertNotNull(response);
            assertFalse(response.isSuccess());
            assertEquals("Order cannot be cancelled after technician has approved it", response.getMessage());
        });
    }

    @Test
    void testGetOrderWhenUserIsNotAuthenticated() throws Exception {
        mockMvc.perform(
                get("/orders")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isForbidden()
        );
    }
}