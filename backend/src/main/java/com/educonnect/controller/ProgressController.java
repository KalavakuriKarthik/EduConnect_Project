package com.educonnect.controller;

import com.educonnect.service.impl.ProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/progress")
@RequiredArgsConstructor
public class ProgressController {

    private final ProgressService progressService;

    @GetMapping("/student/{studentId}")
    public ResponseEntity<?> getProgressByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(progressService.getProgressByStudent(studentId));
    }

    @GetMapping("/student/{studentId}/course/{courseId}")
    public ResponseEntity<?> getProgressByStudentAndCourse(@PathVariable Long studentId,
                                                            @PathVariable Long courseId) {
        return ResponseEntity.ok(progressService.getProgressByStudentAndCourse(studentId, courseId));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<?> getProgressByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(progressService.getProgressByCourse(courseId));
    }
}
