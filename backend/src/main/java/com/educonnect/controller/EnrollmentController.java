package com.educonnect.controller;

import com.educonnect.service.impl.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping("/{studentId}/course/{courseId}")
    @PreAuthorize("hasAnyRole('STUDENT','SCHOOL_ADMIN','PROGRAM_MANAGER')")
    public ResponseEntity<?> enroll(@PathVariable Long studentId, @PathVariable Long courseId) {
        return ResponseEntity.ok(enrollmentService.enroll(studentId, courseId));
    }

    @DeleteMapping("/{studentId}/course/{courseId}")
    @PreAuthorize("hasAnyRole('STUDENT','SCHOOL_ADMIN','PROGRAM_MANAGER')")
    public ResponseEntity<?> unenroll(@PathVariable Long studentId, @PathVariable Long courseId) {
        enrollmentService.unenroll(studentId, courseId);
        return ResponseEntity.ok("Unenrolled successfully");
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<?> getByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByStudent(studentId));
    }

    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasAnyRole('TEACHER','SCHOOL_ADMIN','PROGRAM_MANAGER')")
    public ResponseEntity<?> getByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByCourse(courseId));
    }
}
