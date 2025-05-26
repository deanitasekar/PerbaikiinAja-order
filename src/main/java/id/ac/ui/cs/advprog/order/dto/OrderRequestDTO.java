package id.ac.ui.cs.advprog.order.dto;

import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {
    @NotNull
    private UUID customerId;

    private UUID technicianId;

    @NotBlank(message = "Item name is required")
    private String itemName;

    @NotBlank(message = "Item condition is required")
    private String itemCondition;

    @NotBlank(message = "Repair details are required")
    private String repairDetails;

    @NotNull(message = "Service date is required")
    @FutureOrPresent(message = "Service date must be in the future or present")
    private Date serviceDate;

    @NotNull
    private UUID paymentMethodId;

    private UUID couponId;
}