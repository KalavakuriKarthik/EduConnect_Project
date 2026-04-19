package com.educonnect.dto.response;
import com.educonnect.enums.NotificationCategory;
import com.educonnect.enums.Status;
import lombok.*;
import java.time.LocalDateTime;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class NotificationResponse {
    private Long notificationId;
    private Long userId;
    private Long entityId;
    private String message;
    private NotificationCategory category;
    private Status status;
    private LocalDateTime createdDate;
}
