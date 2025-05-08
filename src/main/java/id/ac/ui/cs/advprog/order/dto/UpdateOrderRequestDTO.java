package id.ac.ui.cs.advprog.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Future;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderRequestDTO {

    private UUID technicianId;

    private String itemName;

    private String itemCondition;

    private String repairDetails;

    @Future(message = "Service date must be in the future")
    private Date desiredServiceDate;

    private String paymentMethod;

    private String couponCode;
}