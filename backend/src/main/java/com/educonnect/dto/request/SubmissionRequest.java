package com.educonnect.dto.request;
import lombok.Data;
@Data
public class SubmissionRequest {
    private Long assessmentId;
    private Long studentId;
    private String fileUri;
}
