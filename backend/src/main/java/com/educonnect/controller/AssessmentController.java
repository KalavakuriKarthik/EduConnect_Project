package com.educonnect.controller;

import com.educonnect.dto.request.AssessmentRequest;
import com.educonnect.dto.request.GradeRequest;
import com.educonnect.dto.request.SubmissionRequest;
import com.educonnect.service.impl.AssessmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/assessments")
@RequiredArgsConstructor
public class AssessmentController {

    private final AssessmentService assessmentService;

    @PostMapping
    @PreAuthorize("hasAnyRole('TEACHER','SCHOOL_ADMIN')")
    public ResponseEntity<?> createAssessment(@Valid @RequestBody AssessmentRequest request) {
        return ResponseEntity.ok(assessmentService.createAssessment(request));
    }

    @GetMapping
    public ResponseEntity<?> getAllAssessments() {
        return ResponseEntity.ok(assessmentService.getAllAssessments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAssessmentById(@PathVariable Long id) {
        return ResponseEntity.ok(assessmentService.getAssessmentById(id));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<?> getAssessmentsByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(assessmentService.getAssessmentsByCourse(courseId));
    }

    @PostMapping("/submit")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> submitAssessment(@RequestBody SubmissionRequest request) {
        return ResponseEntity.ok(assessmentService.submitAssessment(request));
    }

    @PutMapping("/submissions/{submissionId}/grade")
    @PreAuthorize("hasAnyRole('TEACHER','SCHOOL_ADMIN')")
    public ResponseEntity<?> gradeSubmission(@PathVariable Long submissionId,
                                             @RequestBody GradeRequest request) {
        return ResponseEntity.ok(assessmentService.gradeSubmission(submissionId, request));
    }

    @GetMapping("/submissions/student/{studentId}")
    public ResponseEntity<?> getSubmissionsByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(assessmentService.getSubmissionsByStudent(studentId));
    }

    @GetMapping("/submissions/assessment/{assessmentId}")
    @PreAuthorize("hasAnyRole('TEACHER','SCHOOL_ADMIN')")
    public ResponseEntity<?> getSubmissionsByAssessment(@PathVariable Long assessmentId) {
        return ResponseEntity.ok(assessmentService.getSubmissionsByAssessment(assessmentId));
    }
}
