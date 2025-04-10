package id.ac.ui.cs.advprog.order.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import id.ac.ui.cs.advprog.order.model.Notification;
import id.ac.ui.cs.advprog.order.repository.NotificationRepository;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository) {
    }

    @Override
    public Notification createNotification(UUID userId, String title, String message) {
    }

    @Override
    public List<Notification> getNotifications(UUID userId) {
    }

    @Override
    public void deleteNotification(UUID notificationId, UUID userId) throws Exception {
    }
}
