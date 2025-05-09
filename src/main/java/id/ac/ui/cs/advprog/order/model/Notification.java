package id.ac.ui.cs.advprog.order.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Generated;

@Entity
@Table(name = "notifications")
@Generated
@Data
public class Notification {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name="UUID", strategy="org.hibernate.id.UUIDGenerator")
    @Column(name="id", updatable=false, nullable=false)
    private UUID id;

    @Column(name="user_id", nullable=false)
    private UUID userId;

    @Column(name="title", nullable=false)
    private String title;

    @Column(name="message", nullable=false)
    private String message;

    @Column(name="is_read", nullable=false)
    private boolean isRead = false;

    @Column(name="created_at", nullable=false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Notification() {}

    public Notification(UUID userId, String title, String message) {
        this.userId = userId;
        this.title = title;
        this.message = message;
        this.createdAt = LocalDateTime.now();
        this.isRead = false;
    }
}
