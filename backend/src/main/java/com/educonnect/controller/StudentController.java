package com.educonnect.controller;

import com.educonnect.dto.request.StudentProfileRequest;
import com.educonnect.service.impl.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping("/profile/{userId}")
    @PreAuthorize("hasAnyRole('STUDENT','SCHOOL_ADMIN')")
    public ResponseEntity<?> createProfile(@PathVariable Long userId,
                                           @RequestBody StudentProfileRequest request) {
        return ResponseEntity.ok(studentService.createProfile(userId, request));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('TEACHER','SCHOOL_ADMIN','PROGRAM_MANAGER','COMPLIANCE_OFFICER','GOVERNMENT_AUDITOR')")
    public ResponseEntity<?> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<?> getStudentById(@PathVariable Long studentId) {
        return ResponseEntity.ok(studentService.getStudentById(studentId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getStudentByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(studentService.getStudentByUserId(userId));
    }

    @PutMapping("/{studentId}")
    @PreAuthorize("hasAnyRole('STUDENT','SCHOOL_ADMIN')")
    public ResponseEntity<?> updateProfile(@PathVariable Long studentId,
                                           @RequestBody StudentProfileRequest request) {
        return ResponseEntity.ok(studentService.updateProfile(studentId, request));
    }
}
