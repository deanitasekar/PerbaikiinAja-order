package id.ac.ui.cs.advprog.order.model;

import id.ac.ui.cs.advprog.order.enums.OrderStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Generated
@Getter @Setter
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name="UUID", strategy="org.hibernate.id.UUIDGenerator")
    @Column(name="id", updatable=false, nullable=false)
    private UUID id;

    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @Column(name = "technician_id")
    private UUID technicianId;

    @Column(name = "item_name", nullable = false)
    private String itemName;

    @Column(name = "item_condition", nullable = false, columnDefinition = "TEXT")
    private String itemCondition;

    @Column(name = "issue_description", nullable = false, columnDefinition = "TEXT")
    private String issueDescription;

    @Column(name = "desired_service_date", nullable = false)
    private Date desiredServiceDate;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "repair_estimate", columnDefinition = "TEXT")
    private String repairEstimate;

    @Column(name = "repair_price")
    private Double repairPrice;

    @Column(name = "repair_report", columnDefinition = "TEXT")
    private String repairReport;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Order(OrderBuilder builder) {
    }
}