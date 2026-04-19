package com.educonnect.dto.request;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Data
public class AuditRequest {
    @NotBlank private String scope;
    private String findings;
}
