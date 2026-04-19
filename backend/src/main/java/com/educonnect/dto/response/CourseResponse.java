package com.educonnect.dto.response;
import com.educonnect.enums.Status;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CourseResponse {
    private Long courseId;
    private String title;
    private String description;
    private Long teacherId;
    private String teacherName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Status status;
    private long enrollmentCount;
    private LocalDateTime createdAt;
}
