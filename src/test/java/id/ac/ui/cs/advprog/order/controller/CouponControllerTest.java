package id.ac.ui.cs.advprog.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.order.dto.*;
import id.ac.ui.cs.advprog.order.security.JwtAuthenticationFilter;
import id.ac.ui.cs.advprog.order.service.CouponService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = CouponController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)
        }
)
@AutoConfigureMockMvc(addFilters = false)
public class CouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CouponService couponService;

    @Autowired
    private ObjectMapper objectMapper;

    private final UUID couponId = UUID.randomUUID();

    @Test
    void testCreateCoupon() throws Exception {
        CreateCouponRequestDTO request = CreateCouponRequestDTO.builder()
                .coupon_type("FIXED")
                .discount_amount(10000)
                .max_usage(5)
                .start_date(LocalDateTime.now())
                .end_date(LocalDateTime.now().plusDays(7))
                .build();

        CouponResponseDTO response = CouponResponseDTO.builder()
                .id(couponId)
                .code("FIXED-XYZ")
                .coupon_type("FIXED")
                .discount_amount(10000)
                .max_usage(5)
                .start_date(request.getStart_date())
                .end_date(request.getEnd_date())
                .build();

        Mockito.when(couponService.create(any())).thenReturn(response);

        mockMvc.perform(post("/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("FIXED-XYZ"));
    }

    @Test
    void testGetAllCoupons() throws Exception {
        CouponResponseDTO response = CouponResponseDTO.builder()
                .id(couponId)
                .code("FIXED-XYZ")
                .coupon_type("FIXED")
                .discount_amount(10000)
                .max_usage(5)
                .start_date(LocalDateTime.now())
                .end_date(LocalDateTime.now().plusDays(7))
                .build();

        Mockito.when(couponService.findAll()).thenReturn(List.of(response));

        mockMvc.perform(get("/coupons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("FIXED-XYZ"));
    }

    @Test
    void testGetCouponById() throws Exception {
        CouponResponseDTO response = CouponResponseDTO.builder()
                .id(couponId)
                .code("FIXED-XYZ")
                .coupon_type("FIXED")
                .discount_amount(10000)
                .max_usage(5)
                .start_date(LocalDateTime.now())
                .end_date(LocalDateTime.now().plusDays(7))
                .build();

        Mockito.when(couponService.findById(eq(couponId))).thenReturn(response);

        mockMvc.perform(get("/coupons/" + couponId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("FIXED-XYZ"));
    }

    @Test
    void testUpdateCoupon() throws Exception {
        UpdateCouponRequestDTO request = UpdateCouponRequestDTO.builder()
                .coupon_type("FIXED")
                .discount_amount(15000)
                .max_usage(10)
                .start_date(LocalDateTime.now())
                .end_date(LocalDateTime.now().plusDays(10))
                .build();

        CouponResponseDTO updated = CouponResponseDTO.builder()
                .id(couponId)
                .code("FIXED-XYZ")
                .coupon_type("FIXED")
                .discount_amount(15000)
                .max_usage(10)
                .start_date(request.getStart_date())
                .end_date(request.getEnd_date())
                .build();

        Mockito.when(couponService.update(eq(couponId), any())).thenReturn(updated);

        mockMvc.perform(put("/coupons/" + couponId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.discount_amount").value(15000));
    }

    @Test
    void testDeleteCoupon() throws Exception {
        mockMvc.perform(delete("/coupons/" + couponId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Coupon deleted successfully"));
    }

    @Test
    void testCreateCouponInvalidRequest() throws Exception {
        CreateCouponRequestDTO invalid = new CreateCouponRequestDTO(); // empty

        mockMvc.perform(post("/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateCouponNotFound() throws Exception {
        Mockito.when(couponService.update(eq(couponId), any()))
                .thenThrow(new EntityNotFoundException("Coupon not found"));

        mockMvc.perform(put("/coupons/" + couponId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UpdateCouponRequestDTO.builder()
                                .coupon_type("FIXED")
                                .discount_amount(10000)
                                .max_usage(10)
                                .build())))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Coupon not found"));
    }

    @Test
    void testDeleteCouponIllegalState() throws Exception {
        Mockito.doThrow(new IllegalStateException("Cannot delete active coupon"))
                .when(couponService).delete(eq(couponId));

        mockMvc.perform(delete("/coupons/" + couponId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Cannot delete active coupon"));
    }
}
