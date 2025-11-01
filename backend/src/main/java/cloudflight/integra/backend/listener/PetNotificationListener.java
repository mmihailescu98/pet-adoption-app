package cloudflight.integra.backend.listener;

import cloudflight.integra.backend.config.RabbitMQConfig;
import cloudflight.integra.backend.model.User;
import cloudflight.integra.backend.repository.UserRepository;
import cloudflight.integra.backend.service.NotificationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class PetNotificationListener {

    private final NotificationService notificationService;
    private final UserRepository userRepository;

    public PetNotificationListener(NotificationService notificationService, UserRepository userRepository) {
        this.notificationService = notificationService;
        this.userRepository = userRepository;
    }

    @RabbitListener(queues = RabbitMQConfig.NOTIFICATION_QUEUE)
    public void handlePetAddedNotification(Map<String, Object> message) {
        Integer petId = (Integer) message.get("petId");
        Long ownerUserId = ((Number) message.get("ownerUserId")).longValue();

        List<User> allUsers = userRepository.findAll();

        String title = "New Pet Available!";
        String notificationMessage = "A new pet is now available for adoption!";

        for (User user : allUsers) {
            if (!user.getId().equals(ownerUserId)) {
                notificationService.createNotification(user.getId(), title, notificationMessage, petId);
            }
        }
    }
}

