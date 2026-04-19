package com.educonnect.dto.response;
import com.educonnect.enums.Status;
import lombok.*;
import java.time.LocalDate;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class StudentResponse {
    private Long studentId;
    private Long userId;
    private String name;
    private String email;
    private LocalDate dob;
    private String gender;
    private String address;
    private String contactInfo;
    private Status status;
}
