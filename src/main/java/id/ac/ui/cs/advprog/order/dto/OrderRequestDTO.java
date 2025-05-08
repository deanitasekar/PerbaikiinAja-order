package id.ac.ui.cs.advprog.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Future;
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

    @NotBlank(message = "Repair details is required")
    private String repairDetails;

    @NotNull(message = "Service date is required")
    @Future(message = "Service date must be in the future")
    private Date serviceDate;

    @NotBlank(message = "Payment method is required")
    private String paymentMethod;

    private String couponCode;
}