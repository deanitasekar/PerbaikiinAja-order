package id.ac.ui.cs.advprog.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import id.ac.ui.cs.advprog.order.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {
    private UUID id;
    private UUID customerId;
    private UUID technicianId;
    private String itemName;
    private String itemCondition;
    private String repairDetails;
    private Date serviceDate;
    private OrderStatus status;
    private UUID paymentMethodId;
    private UUID couponId;
    private String estimatedCompletionTime;
    private BigDecimal estimatedPrice;
    private BigDecimal finalPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime completedAt;
}