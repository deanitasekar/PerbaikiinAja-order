package id.ac.ui.cs.advprog.order.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

public class NotificationTest {

    @Test
    void testNotificationCreationDefault() {
        UUID userId = UUID.randomUUID();
        String title = "Repair Completed";
        String message = "Your repair has been completed successfully.";
        Notification notification = new Notification(userId, title, message);

        assertEquals(userId, notification.getUserId());
        assertEquals(title, notification.getTitle());
        assertEquals(message, notification.getMessage());
        assertFalse(notification.isRead());
        assertNotNull(notification.getCreatedAt());
    }

    @Test
    void testNotificationSetters() {
        Notification notification = new Notification();
        UUID userId = UUID.randomUUID();
        notification.setUserId(userId);
        notification.setTitle("New Title");
        notification.setMessage("New Message");
        notification.setRead(true);
        LocalDateTime now = LocalDateTime.now();
        notification.setCreatedAt(now);
        UUID newId = UUID.randomUUID();
        notification.setId(newId);

        assertEquals(userId, notification.getUserId());
        assertEquals("New Title", notification.getTitle());
        assertEquals("New Message", notification.getMessage());
        assertTrue(notification.isRead());
        assertEquals(now, notification.getCreatedAt());
        assertEquals(newId, notification.getId());
    }
}
