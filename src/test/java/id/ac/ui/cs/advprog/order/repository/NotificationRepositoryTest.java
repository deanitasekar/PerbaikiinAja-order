package id.ac.ui.cs.advprog.order.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import id.ac.ui.cs.advprog.order.model.Notification;

@DataJpaTest
public class NotificationRepositoryTest {

    @Autowired
    private NotificationRepository notificationRepository;

    @Test
    void testSaveAndFindByUserId() {
        UUID userId = UUID.randomUUID();
        String title = "Repair Completed";
        String message = "Your repair is complete.";
        Notification notification = new Notification(userId, title, message);
        notification = notificationRepository.save(notification);
        assertNotNull(notification.getId());

        List<Notification> notifications = notificationRepository.findByUserId(userId);
        assertFalse(notifications.isEmpty());
        assertEquals(title, notifications.get(0).getTitle());
        assertEquals(message, notifications.get(0).getMessage());
    }
}
