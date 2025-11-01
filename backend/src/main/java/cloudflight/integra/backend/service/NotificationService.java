package cloudflight.integra.backend.service;

import cloudflight.integra.backend.model.Notification;

import java.util.List;

public interface NotificationService {
    void createNotification(Long userId, String title, String message, Integer petId);
    List<Notification> getUserNotifications(Long userId);
    List<Notification> getUnreadNotifications(Long userId);
    long getUnreadCount(Long userId);
    void markAsRead(Long notificationId, Long userId) throws Exception;
    void markAllAsRead(Long userId);
    void publishPetAddedNotification(Integer petId, Long ownerUserId);
}