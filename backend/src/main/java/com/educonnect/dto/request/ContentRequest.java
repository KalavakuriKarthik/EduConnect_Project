package com.educonnect.dto.request;
import com.educonnect.enums.ContentType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Data
public class ContentRequest {
    @NotBlank private String title;
    private ContentType type;
    private Long courseId;
    private String fileUri;
}
