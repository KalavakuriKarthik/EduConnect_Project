package com.educonnect.dto.response;
import com.educonnect.enums.Status;
import lombok.*;
import java.time.LocalDateTime;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class SubmissionResponse {
    private Long submissionId;
    private Long assessmentId;
    private String assessmentTitle;
    private Long studentId;
    private String studentName;
    private String fileUri;
    private Double grade;
    private String feedback;
    private LocalDateTime date;
    private Status status;
}
