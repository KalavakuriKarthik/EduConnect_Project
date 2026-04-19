package com.educonnect.dto.request;
import com.educonnect.enums.AssessmentType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDate;
@Data
public class AssessmentRequest {
    @NotBlank private String title;
    private AssessmentType type;
    private Long courseId;
    private LocalDate date;
    private String description;
}
