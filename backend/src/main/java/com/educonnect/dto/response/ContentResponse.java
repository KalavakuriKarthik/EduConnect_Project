package com.educonnect.dto.response;
import com.educonnect.enums.ContentType;
import com.educonnect.enums.Status;
import lombok.*;
import java.time.LocalDateTime;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ContentResponse {
    private Long contentId;
    private Long courseId;
    private String courseTitle;
    private String title;
    private ContentType type;
    private String fileUri;
    private LocalDateTime uploadedDate;
    private Status status;
    private long accessCount;
}
