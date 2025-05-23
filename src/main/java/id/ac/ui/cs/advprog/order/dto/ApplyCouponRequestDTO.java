package id.ac.ui.cs.advprog.order.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplyCouponRequestDTO {
    @Min(value = 0, message = "Original price must be ≥ 0")
    private double original_price;
}
