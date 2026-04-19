package com.educonnect.dto.request;
import com.educonnect.enums.Role;
import jakarta.validation.constraints.*;
import lombok.Data;
@Data
public class RegisterRequest {
    @NotBlank private String name;
    @NotBlank @Email private String email;
    @NotBlank @Size(min=6) private String password;
    private String phone;
    @NotNull private Role role;
}
