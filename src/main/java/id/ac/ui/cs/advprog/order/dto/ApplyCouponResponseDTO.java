package id.ac.ui.cs.advprog.order.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class ApplyCouponResponseDTO {
    private UUID id;
    private String coupon_code;
    private double original_price;
    private double discounted_price;
    private boolean valid;
}
