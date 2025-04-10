package id.ac.ui.cs.advprog.order.model;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Generated;

@Entity
@Table(name = "notifications")
@Generated
@Data
public class Notification {

    public Notification() {}

    public Notification(UUID userId, String title, String message) {
    }
}
