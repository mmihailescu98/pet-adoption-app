package cloudflight.integra.backend.mapper;

import cloudflight.integra.backend.dto.NotificationDTO;
import cloudflight.integra.backend.model.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface NotificationMapper {
    NotificationMapper INSTANCE = Mappers.getMapper(NotificationMapper.class);

    @Mapping(source = "read", target = "isRead")
    @Mapping(source = "pet.id", target = "petId")
    @Mapping(source = "pet.name", target = "petName")
    @Mapping(source = "pet.species", target = "petSpecies")
    NotificationDTO notificationToNotificationDTO(Notification notification);

    List<NotificationDTO> notificationToNotificationDTOList(List<Notification> notifications);
}
