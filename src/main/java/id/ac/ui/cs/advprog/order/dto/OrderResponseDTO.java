package id.ac.ui.cs.advprog.order.dto;

import id.ac.ui.cs.advprog.order.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {
    private UUID id;
    private UUID customerId;
    private UUID technicianId;
    private String itemName;
    private String itemCondition;
    private String repairDetails;
    private Date desiredServiceDate;
    private OrderStatus status;
    private String paymentMethod;
    private String couponCode;
    private String repairEstimate;
    private Double repairPrice;
    private String repairReport;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String technicianName;
    private Double technicianRating;
}