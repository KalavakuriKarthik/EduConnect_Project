package com.educonnect.dto.response;
import lombok.*;
import java.time.LocalDateTime;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ProgressResponse {
    private Long progressId;
    private Long studentId;
    private String studentName;
    private Long courseId;
    private String courseTitle;
    private Double completionPercentage;
    private String metricsJson;
    private LocalDateTime date;
}
