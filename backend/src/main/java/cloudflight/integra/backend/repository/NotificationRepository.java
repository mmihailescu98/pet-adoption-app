package cloudflight.integra.backend.repository;

import cloudflight.integra.backend.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserIdOrderByTimestampDesc(Long userId);
    List<Notification> findByUserIdAndReadFalseOrderByTimestampDesc(Long userId);
    long countByUserIdAndReadFalse(Long userId);
}