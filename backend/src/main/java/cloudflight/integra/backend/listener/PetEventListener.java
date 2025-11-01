package cloudflight.integra.backend.listener;

import cloudflight.integra.backend.service.NotificationService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class PetEventListener {

    private final NotificationService notificationService;

    PetEventListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @EventListener
    @Async
    public void handlePetAdded(PetAddedEvent event) {
        notificationService.publishPetAddedNotification(
                event.getPetId(),
                event.getOwnerUserId()
        );
    }
}