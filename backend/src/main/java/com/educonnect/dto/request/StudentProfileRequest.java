package com.educonnect.dto.request;
import lombok.Data;
import java.time.LocalDate;
@Data
public class StudentProfileRequest {
    private String name;
    private LocalDate dob;
    private String gender;
    private String address;
    private String contactInfo;
}
