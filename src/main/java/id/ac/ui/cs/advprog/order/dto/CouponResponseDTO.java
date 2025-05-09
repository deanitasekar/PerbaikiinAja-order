package id.ac.ui.cs.advprog.order.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponResponseDTO {
    private UUID id;
    private String code;
    private String coupon_type;
    private double discount_amount;
    private int max_usage;
    private LocalDateTime start_date;
    private LocalDateTime end_date;
}
