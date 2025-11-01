package cloudflight.integra.backend.service.impl;

import cloudflight.integra.backend.config.RabbitMQConfig;
import cloudflight.integra.backend.model.Notification;
import cloudflight.integra.backend.model.Pet;
import cloudflight.integra.backend.model.User;
import cloudflight.integra.backend.repository.NotificationRepository;
import cloudflight.integra.backend.repository.PetRepository;
import cloudflight.integra.backend.repository.UserRepository;
import cloudflight.integra.backend.service.NotificationService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final PetRepository petRepository;
    private final RabbitTemplate rabbitTemplate;

    public NotificationServiceImpl(NotificationRepository notificationRepository, UserRepository userRepository, PetRepository petRepository, RabbitTemplate rabbitTemplate) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.petRepository = petRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void createNotification(Long userId, String title, String message, Integer petId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return;
        }

        Pet pet = petId != null ? petRepository.findById(petId).orElse(null) : null;

        Notification notification = Notification.builder()
                .user(user)
                .title(title)
                .message(message)
                .timestamp(LocalDateTime.now())
                .read(false)
                .pet(pet)
                .build();

        notificationRepository.save(notification);
    }

    @Override
    public List<Notification> getUserNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByTimestampDesc(userId);
    }

    @Override
    public List<Notification> getUnreadNotifications(Long userId) {
        return notificationRepository.findByUserIdAndReadFalseOrderByTimestampDesc(userId);
    }

    @Override
    public long getUnreadCount(Long userId) {
        return notificationRepository.countByUserIdAndReadFalse(userId);
    }

    @Override
    public void markAsRead(Long notificationId, Long userId) throws Exception {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> new Exception("Notification not found"));

        if (!notification.getUser().getId().equals(userId)) {
            throw new Exception("Unauthorized access to notification");
        }

        notification.setRead(true);
        notificationRepository.save(notification);
    }

    @Override
    public void markAllAsRead(Long userId) {
        List<Notification> unreadNotifications = notificationRepository.findByUserIdAndReadFalseOrderByTimestampDesc(userId);

        unreadNotifications.forEach(notification -> notification.setRead(true));
        notificationRepository.saveAll(unreadNotifications);
    }

    @Override
    public void publishPetAddedNotification(Integer petId, Long ownerUserId) {
        Map<String, Object> message = new HashMap<>();
        message.put("petId", petId);
        message.put("ownerUserId", ownerUserId);
        message.put("timestamp", LocalDateTime.now().toString());

        rabbitTemplate.convertAndSend(RabbitMQConfig.NOTIFICATION_EXCHANGE, RabbitMQConfig.NOTIFICATION_ROUTING_KEY, message);
    }
}

