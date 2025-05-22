package id.ac.ui.cs.advprog.order.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class CreateCouponRequestDTO {
    @NotBlank(message = "Coupon type is required")
    private String couponType;

    @PositiveOrZero(message = "Discount must be ≥ 0")
    private double discount_amount;

    @Min(value = 1, message = "Max usage must be at least 1")
    private int max_usage;

    private LocalDateTime start_date;
    private LocalDateTime end_date;
}
