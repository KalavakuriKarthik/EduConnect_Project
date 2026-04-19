package com.educonnect.dto.request;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Data
public class ComplianceRecordRequest {
    private Long entityId;
    @NotBlank private String type;
    @NotBlank private String result;
    private String notes;
}
