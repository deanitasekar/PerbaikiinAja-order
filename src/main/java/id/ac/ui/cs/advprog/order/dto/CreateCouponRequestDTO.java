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
    private String type;

    @PositiveOrZero(message = "Discount must be ≥ 0")
    private double discount;

    @Min(value = 1, message = "Max usage must be at least 1")
    private int maxUsage;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
