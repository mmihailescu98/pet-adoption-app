package cloudflight.integra.backend.controller;

import cloudflight.integra.backend.dto.NotificationDTO;
import cloudflight.integra.backend.mapper.NotificationMapper;
import cloudflight.integra.backend.security.CustomUserDetails;
import cloudflight.integra.backend.security.JwtUtil;
import cloudflight.integra.backend.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<NotificationDTO>> getUserNotifications() {
        CustomUserDetails authUser = JwtUtil.getAuthenticatedUser();
        if (authUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(NotificationMapper.INSTANCE.notificationToNotificationDTOList(notificationService.getUserNotifications(authUser.getId())));
    }

    @GetMapping(value = "/unread", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<NotificationDTO>> getUnreadNotifications() {
        CustomUserDetails authUser = JwtUtil.getAuthenticatedUser();
        if (authUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(NotificationMapper.INSTANCE.notificationToNotificationDTOList(notificationService.getUnreadNotifications(authUser.getId())));
    }

    @GetMapping(value = "/unread/count", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Long>> getUnreadCount() {
        CustomUserDetails authUser = JwtUtil.getAuthenticatedUser();
        if (authUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        long count = notificationService.getUnreadCount(authUser.getId());
        Map<String, Long> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        CustomUserDetails authUser = JwtUtil.getAuthenticatedUser();
        if (authUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            notificationService.markAsRead(id, authUser.getId());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PutMapping("/read-all")
    public ResponseEntity<Void> markAllAsRead() {
        CustomUserDetails authUser = JwtUtil.getAuthenticatedUser();
        if (authUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        notificationService.markAllAsRead(authUser.getId());
        return ResponseEntity.ok().build();
    }
}
