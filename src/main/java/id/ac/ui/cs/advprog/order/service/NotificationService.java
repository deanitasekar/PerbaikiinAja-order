package id.ac.ui.cs.advprog.order.service;

import java.util.List;
import java.util.UUID;

import id.ac.ui.cs.advprog.order.model.Notification;

public interface NotificationService {

    Notification createNotification(UUID userId, String title, String message);

    List<Notification> getNotifications(UUID userId);

    void deleteNotification(UUID notificationId, UUID userId) throws Exception;
}
