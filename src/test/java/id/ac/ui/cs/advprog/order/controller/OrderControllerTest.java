package id.ac.ui.cs.advprog.order.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import id.ac.ui.cs.advprog.order.dto.OrderRequestDTO;
import id.ac.ui.cs.advprog.order.dto.UpdateOrderRequestDTO;
import id.ac.ui.cs.advprog.order.dto.OrderResponseDTO;
import id.ac.ui.cs.advprog.order.dto.OrderListResponseDTO;
import id.ac.ui.cs.advprog.order.dto.ResponseDTO;
import id.ac.ui.cs.advprog.order.service.OrderService;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;
import java.util.concurrent.CompletableFuture;

class OrderControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @Mock
    private Authentication auth;

    @InjectMocks
    private OrderController orderController;

    private UUID userId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userId = UUID.randomUUID();
        Mockito.when(auth.getPrincipal()).thenReturn(userId.toString());
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    void testCreateOrder_success() throws Exception {
        OrderResponseDTO respDto = new OrderResponseDTO();
        respDto.setId(UUID.randomUUID());
        respDto.setCustomerId(userId);

        Mockito.when(orderService.createOrder(any(OrderRequestDTO.class)))
                .thenReturn(CompletableFuture.completedFuture(respDto));

        MvcResult mvc = mockMvc.perform(post("/orders")
                        .principal(auth)
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(request().asyncStarted())
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvc))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.order.id").value(respDto.getId().toString()))
                .andExpect(jsonPath("$.message").value("Order created successfully"));
    }

    @Test
    void testGetOrderHistory_success() throws Exception {
        OrderResponseDTO single = new OrderResponseDTO();
        single.setId(UUID.randomUUID());
        single.setCustomerId(userId);

        OrderListResponseDTO listDto = new OrderListResponseDTO();
        listDto.setOrders(List.of(single));
        listDto.setCount(1);

        Mockito.when(orderService.getOrdersByCustomerId(userId))
                .thenReturn(CompletableFuture.completedFuture(listDto));

        MvcResult mvc = mockMvc.perform(get("/orders").principal(auth))
                .andExpect(request().asyncStarted())
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvc))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orders[0].id").value(single.getId().toString()))
                .andExpect(jsonPath("$.count").value(1))
                .andExpect(jsonPath("$.message").value("Orders retrieved successfully"));
    }

    @Test
    void testGetOrderDetail_success() throws Exception {
        UUID orderId = UUID.randomUUID();
        OrderResponseDTO respDto = new OrderResponseDTO();
        respDto.setId(orderId);
        respDto.setCustomerId(userId);

        Mockito.when(orderService.getOrderById(orderId))
                .thenReturn(CompletableFuture.completedFuture(respDto));

        MvcResult mvc = mockMvc.perform(get("/orders/{orderId}", orderId).principal(auth))
                .andExpect(request().asyncStarted())
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvc))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.order.id").value(orderId.toString()))
                .andExpect(jsonPath("$.message").value("Order retrieved successfully"));
    }

    @Test
    void testUpdateOrder_success() throws Exception {
        UUID orderId = UUID.randomUUID();
        OrderResponseDTO existing = new OrderResponseDTO(); existing.setId(orderId); existing.setCustomerId(userId);
        OrderResponseDTO updated = new OrderResponseDTO(); updated.setId(orderId); updated.setCustomerId(userId);

        Mockito.when(orderService.getOrderById(orderId))
                .thenReturn(CompletableFuture.completedFuture(existing));
        Mockito.when(orderService.updateOrder(eq(orderId), any(UpdateOrderRequestDTO.class)))
                .thenReturn(CompletableFuture.completedFuture(updated));

        MvcResult mvc = mockMvc.perform(put("/orders/{orderId}", orderId)
                        .principal(auth)
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(request().asyncStarted())
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvc))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.order.id").value(orderId.toString()))
                .andExpect(jsonPath("$.message").value("Order ID " + orderId + " updated successfully"));
    }

    @Test
    void testCancelOrder_success() throws Exception {
        UUID orderId = UUID.randomUUID();
        OrderResponseDTO existing = new OrderResponseDTO(); existing.setId(orderId); existing.setCustomerId(userId);
        ResponseDTO cancelResp = new ResponseDTO(); cancelResp.setSuccess(true); cancelResp.setMessage("Canceled successfully");

        Mockito.when(orderService.getOrderById(orderId)).thenReturn(CompletableFuture.completedFuture(existing));
        Mockito.when(orderService.cancelOrder(orderId)).thenReturn(CompletableFuture.completedFuture(cancelResp));

        MvcResult mvc = mockMvc.perform(delete("/orders/{orderId}", orderId).principal(auth))
                .andExpect(request().asyncStarted())
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvc))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Canceled successfully"));
    }

    @Test
    void testCreateOrder_InternalError() throws Exception {
        Mockito.when(orderService.createOrder(any(OrderRequestDTO.class)))
                .thenReturn(CompletableFuture.failedFuture(new RuntimeException("oops")));

        MvcResult mvc = mockMvc.perform(post("/orders").principal(auth)
                        .contentType("application/json").content("{}"))
                .andExpect(request().asyncStarted())
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvc))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .andExpect(jsonPath("$.error").value("oops"))
                .andExpect(jsonPath("$.message").value("Something went wrong with the server"));
    }

    @Test
    void testGetOrderHistory_InternalError() throws Exception {
        Mockito.when(orderService.getOrdersByCustomerId(userId))
                .thenReturn(CompletableFuture.failedFuture(new RuntimeException("oops")));

        MvcResult mvc = mockMvc.perform(get("/orders").principal(auth))
                .andExpect(request().asyncStarted())
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvc))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .andExpect(jsonPath("$.error").value("oops"))
                .andExpect(jsonPath("$.message").value("Something went wrong with the server"));
    }

    @Test
    void testGetOrderDetail_Forbidden() throws Exception {
        UUID orderId = UUID.randomUUID();
        OrderResponseDTO dto = new OrderResponseDTO(); dto.setId(orderId); dto.setCustomerId(UUID.randomUUID());
        Mockito.when(orderService.getOrderById(orderId))
                .thenReturn(CompletableFuture.completedFuture(dto));

        MvcResult mvc = mockMvc.perform(get("/orders/{orderId}", orderId).principal(auth))
                .andExpect(request().asyncStarted())
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvc))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(HttpStatus.FORBIDDEN.value()))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("You are not authorized to access this resource"));
    }

    @Test
    void testGetOrderDetail_NotFound_HTTP() throws Exception {
        UUID orderId = UUID.randomUUID();
        Mockito.when(orderService.getOrderById(orderId))
                .thenReturn(CompletableFuture.failedFuture(new EntityNotFoundException("Order not found with ID: " + orderId)));

        MvcResult mvc = mockMvc.perform(get("/orders/{orderId}", orderId).principal(auth))
                .andExpect(request().asyncStarted())
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvc))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Order not found with ID: " + orderId));
    }

    @Test
    void testUpdateOrder_NotFound_HTTP() throws Exception {
        UUID orderId = UUID.randomUUID();
        Mockito.when(orderService.getOrderById(orderId))
                .thenReturn(CompletableFuture.failedFuture(new EntityNotFoundException("Order not found with ID: " + orderId)));

        MvcResult mvc = mockMvc.perform(put("/orders/{orderId}", orderId)
                        .principal(auth)
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(request().asyncStarted())
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvc))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Order not found with ID: " + orderId));
    }

    @Test
    void testUpdateOrder_BadRequest_HTTP() throws Exception {
        UUID orderId = UUID.randomUUID();
        OrderResponseDTO existing = new OrderResponseDTO(); existing.setId(orderId); existing.setCustomerId(userId);
        Mockito.when(orderService.getOrderById(orderId))
                .thenReturn(CompletableFuture.completedFuture(existing));
        Mockito.when(orderService.updateOrder(eq(orderId), any(UpdateOrderRequestDTO.class)))
                .thenReturn(CompletableFuture.failedFuture(new IllegalStateException("cannot update")));

        MvcResult mvc = mockMvc.perform(put("/orders/{orderId}", orderId)
                        .principal(auth)
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(request().asyncStarted())
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvc))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(HttpStatus.FORBIDDEN.value()))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("cannot update"));
    }

    @Test
    void testUpdateOrder_Forbidden_HTTP() throws Exception {
        UUID orderId = UUID.randomUUID();
        OrderResponseDTO wrongOwner = new OrderResponseDTO(); wrongOwner.setId(orderId); wrongOwner.setCustomerId(UUID.randomUUID());
        Mockito.when(orderService.getOrderById(orderId))
                .thenReturn(CompletableFuture.completedFuture(wrongOwner));

        MvcResult mvc = mockMvc.perform(put("/orders/{orderId}", orderId)
                        .principal(auth)
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(request().asyncStarted())
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvc))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(HttpStatus.FORBIDDEN.value()))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("You are not authorized to access this resource"));
    }

    @Test
    void testCancelOrder_NotFound_HTTP() throws Exception {
        UUID orderId = UUID.randomUUID();
        Mockito.when(orderService.getOrderById(orderId))
                .thenReturn(CompletableFuture.failedFuture(new EntityNotFoundException("Order not found with ID: " + orderId)));

        MvcResult mvc = mockMvc.perform(delete("/orders/{orderId}", orderId)
                        .principal(auth))
                .andExpect(request().asyncStarted())
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvc))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Order not found with ID: " + orderId));
    }

    @Test
    void testCancelOrder_BadRequest_HTTP() throws Exception {
        UUID orderId = UUID.randomUUID();
        OrderResponseDTO existing = new OrderResponseDTO(); existing.setId(orderId); existing.setCustomerId(userId);
        Mockito.when(orderService.getOrderById(orderId)).thenReturn(CompletableFuture.completedFuture(existing));
        Mockito.when(orderService.cancelOrder(orderId))
                .thenReturn(CompletableFuture.failedFuture(new IllegalStateException("cannot cancel")));

        MvcResult mvc = mockMvc.perform(delete("/orders/{orderId}", orderId)
                        .principal(auth))
                .andExpect(request().asyncStarted())
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvc))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(HttpStatus.FORBIDDEN.value()))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("cannot cancel"));
    }

    @Test
    void testCancelOrder_Forbidden_HTTP() throws Exception {
        UUID orderId = UUID.randomUUID();
        OrderResponseDTO wrongOwner = new OrderResponseDTO(); wrongOwner.setId(orderId); wrongOwner.setCustomerId(UUID.randomUUID());
        Mockito.when(orderService.getOrderById(orderId)).thenReturn(CompletableFuture.completedFuture(wrongOwner));

        MvcResult mvc = mockMvc.perform(delete("/orders/{orderId}", orderId)
                        .principal(auth))
                .andExpect(request().asyncStarted())
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvc))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(HttpStatus.FORBIDDEN.value()))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("You are not authorized to access this resource"));
    }
}
