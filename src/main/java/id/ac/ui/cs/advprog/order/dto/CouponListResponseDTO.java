package id.ac.ui.cs.advprog.order.dto;

import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponListResponseDTO {
    private List<CouponResponseDTO> coupons;
    private int total;
}
