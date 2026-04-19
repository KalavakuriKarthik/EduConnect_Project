package com.educonnect.dto.response;
import com.educonnect.enums.AssessmentType;
import com.educonnect.enums.Status;
import lombok.*;
import java.time.LocalDate;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class AssessmentResponse {
    private Long assessmentId;
    private String title;
    private AssessmentType type;
    private Long courseId;
    private String courseTitle;
    private LocalDate date;
    private String description;
    private Status status;
    private int submissionCount;
}
