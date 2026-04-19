package com.educonnect.dto.response;
import com.educonnect.enums.Role;
import com.educonnect.enums.Status;
import lombok.*;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class UserResponse {
    private Long userId;
    private String name;
    private String email;
    private String phone;
    private Role role;
    private Status status;
}
