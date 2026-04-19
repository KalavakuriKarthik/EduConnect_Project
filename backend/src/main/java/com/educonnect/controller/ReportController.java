package com.educonnect.controller;

import com.educonnect.service.impl.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('SCHOOL_ADMIN','PROGRAM_MANAGER','GOVERNMENT_AUDITOR','COMPLIANCE_OFFICER')")
    public ResponseEntity<?> getDashboard() {
        return ResponseEntity.ok(reportService.getDashboardMetrics());
    }

    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasAnyRole('TEACHER','SCHOOL_ADMIN','PROGRAM_MANAGER')")
    public ResponseEntity<?> getCourseReport(@PathVariable Long courseId) {
        return ResponseEntity.ok(reportService.getCourseReport(courseId));
    }

    @PostMapping("/generate")
    @PreAuthorize("hasAnyRole('SCHOOL_ADMIN','PROGRAM_MANAGER','GOVERNMENT_AUDITOR')")
    public ResponseEntity<?> generateReport(@RequestParam String scope,
                                            @RequestParam(required = false) Long generatedBy) {
        return ResponseEntity.ok(reportService.generateReport(scope, generatedBy));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SCHOOL_ADMIN','PROGRAM_MANAGER','GOVERNMENT_AUDITOR','COMPLIANCE_OFFICER')")
    public ResponseEntity<?> getAllReports() {
        return ResponseEntity.ok(reportService.getAllReports());
    }

    @GetMapping("/scope/{scope}")
    public ResponseEntity<?> getReportsByScope(@PathVariable String scope) {
        return ResponseEntity.ok(reportService.getReportsByScope(scope));
    }
}
