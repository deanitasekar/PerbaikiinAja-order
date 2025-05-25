package id.ac.ui.cs.advprog.order.dto;

import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.UUID;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import id.ac.ui.cs.advprog.order.enums.OrderStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderRequestDTO {
    private String itemName;
    private String itemCondition;
    private String repairDetails;

    private UUID technicianId;

    @FutureOrPresent(message = "Service date must be in the future or present")
    private Date serviceDate;

    private UUID paymentMethodId;
    private UUID couponId;

    private String estimatedCompletionTime;
    private BigDecimal estimatedPrice;
    private BigDecimal finalPrice;

    private LocalDateTime completedAt;
}
