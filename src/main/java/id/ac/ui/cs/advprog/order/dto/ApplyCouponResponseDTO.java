package id.ac.ui.cs.advprog.order.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApplyCouponResponseDTO {
    private double original_price;
    private double discounted_price;
    private String coupon_code;
    private boolean applied;
}
