package com.educonnect.service.impl;

import com.educonnect.dto.response.NotificationResponse;
import com.educonnect.entity.Notification;
import com.educonnect.entity.User;
import com.educonnect.enums.NotificationCategory;
import com.educonnect.enums.Status;
import com.educonnect.exception.ResourceNotFoundException;
import com.educonnect.repository.NotificationRepository;
import com.educonnect.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public List<NotificationResponse> getNotificationsByUser(Long userId) {
        return notificationRepository.findByUser_UserId(userId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<NotificationResponse> getUnreadByUser(Long userId) {
        return notificationRepository.findByUser_UserIdAndStatus(userId, Status.ACTIVE)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public Map<String, Long> getUnreadCount(Long userId) {
        long count = notificationRepository.countByUser_UserIdAndStatus(userId, Status.ACTIVE);
        return Map.of("unreadCount", count);
    }

    @Transactional
    public void markAsRead(Long notificationId) {
        Notification n = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", notificationId));
        n.setStatus(Status.INACTIVE);
        notificationRepository.save(n);
    }

    @Transactional
    public void markAllAsRead(Long userId) {
        List<Notification> unread = notificationRepository.findByUser_UserIdAndStatus(userId, Status.ACTIVE);
        unread.forEach(n -> n.setStatus(Status.INACTIVE));
        notificationRepository.saveAll(unread);
    }

    @Transactional
    public NotificationResponse sendNotification(Long userId, String message, NotificationCategory category, Long entityId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        Notification notification = Notification.builder()
                .user(user).message(message).category(category)
                .entityId(entityId).status(Status.ACTIVE).build();
        return toResponse(notificationRepository.save(notification));
    }

    private NotificationResponse toResponse(Notification n) {
        return NotificationResponse.builder()
                .notificationId(n.getNotificationId())
                .userId(n.getUser().getUserId())
                .entityId(n.getEntityId())
                .message(n.getMessage())
                .category(n.getCategory())
                .status(n.getStatus())
                .createdDate(n.getCreatedDate())
                .build();
    }
}
