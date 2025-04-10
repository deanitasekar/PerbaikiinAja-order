package id.ac.ui.cs.advprog.order.service;

import java.util.List;
import java.util.Optional;
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
        this.notificationRepository = notificationRepository;
    }

    @Override
    public Notification createNotification(UUID userId, String title, String message) {
        Notification notification = new Notification(userId, title, message);
        return notificationRepository.save(notification);
    }

    @Override
    public List<Notification> getNotifications(UUID userId) {
        List<Notification> notifications = notificationRepository.findByUserId(userId);
        notifications.forEach(notification -> {
            if (!notification.isRead()) {
                notification.setRead(true);
                notificationRepository.save(notification);
            }
        });
        return notifications;
    }

    @Override
    public void deleteNotification(UUID notificationId, UUID userId) throws Exception {
        Optional<Notification> optional = notificationRepository.findById(notificationId);
        if (optional.isEmpty()) {
            throw new Exception("Notification not found");
        }
        Notification notification = optional.get();
        if (!notification.getUserId().equals(userId)) {
            throw new Exception("Not authorized to delete this notification");
        }
        notificationRepository.deleteById(notificationId);
    }
}
