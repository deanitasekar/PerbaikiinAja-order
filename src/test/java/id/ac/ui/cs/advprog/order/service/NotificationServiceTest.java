package id.ac.ui.cs.advprog.order.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import id.ac.ui.cs.advprog.order.model.Notification;
import id.ac.ui.cs.advprog.order.repository.NotificationRepository;

public class NotificationServiceTest {

    private NotificationRepository notificationRepository;
    private NotificationServiceImpl notificationService;

    @BeforeEach
    void setUp() {
        notificationRepository = Mockito.mock(NotificationRepository.class);
        notificationService = new NotificationServiceImpl(notificationRepository);
    }

    @Test
    void testCreateNotification() {
        UUID userId = UUID.randomUUID();
        String title = "Repair Completed";
        String message = "Your repair is complete.";
        Notification notification = new Notification(userId, title, message);
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        Notification created = notificationService.createNotification(userId, title, message);
        assertNotNull(created);
        assertEquals(userId, created.getUserId());
        assertEquals(title, created.getTitle());
        assertEquals(message, created.getMessage());
        assertFalse(created.isRead());
    }

    @Test
    void testGetNotificationsMarksAsRead() {
        UUID userId = UUID.randomUUID();
        String title = "Repair Completed";
        String message = "Your repair is complete.";
        Notification n1 = new Notification(userId, title, message);
        Notification n2 = new Notification(userId, title, message);
        n1.setRead(false);
        n2.setRead(false);
        when(notificationRepository.findByUserId(userId)).thenReturn(Arrays.asList(n1, n2));
        when(notificationRepository.save(any(Notification.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var notifications = notificationService.getNotifications(userId);
        for (Notification n : notifications) {
            assertTrue(n.isRead());
        }
    }

    @Test
    void testDeleteNotificationSuccess() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID notificationId = UUID.randomUUID();
        Notification notification = new Notification(userId, "Title", "Message");
        notification.setId(notificationId);
        when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(notification));
        doNothing().when(notificationRepository).deleteById(notificationId);

        assertDoesNotThrow(() -> notificationService.deleteNotification(notificationId, userId));
        verify(notificationRepository, times(1)).deleteById(notificationId);
    }

    @Test
    void testDeleteNotificationNotFound() {
        UUID userId = UUID.randomUUID();
        UUID notificationId = UUID.randomUUID();
        when(notificationRepository.findById(notificationId)).thenReturn(Optional.empty());

        Exception ex = assertThrows(Exception.class, () -> notificationService.deleteNotification(notificationId, userId));
        assertTrue(ex.getMessage().contains("Notification not found"));
    }

    @Test
    void testDeleteNotificationNotAuthorized() {
        UUID userId = UUID.randomUUID();
        UUID otherUserId = UUID.randomUUID();
        UUID notificationId = UUID.randomUUID();
        Notification notification = new Notification(otherUserId, "Title", "Message");
        notification.setId(notificationId);
        when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(notification));

        Exception ex = assertThrows(Exception.class, () -> notificationService.deleteNotification(notificationId, userId));
        assertTrue(ex.getMessage().contains("Not authorized"));
    }
}
