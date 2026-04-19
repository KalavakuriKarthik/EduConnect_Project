package com.educonnect.controller;

import com.educonnect.service.impl.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService auditLogService;

    @GetMapping
    @PreAuthorize("hasAnyRole('SCHOOL_ADMIN','GOVERNMENT_AUDITOR','COMPLIANCE_OFFICER')")
    public ResponseEntity<?> getAllLogs() {
        return ResponseEntity.ok(auditLogService.getAllLogs());
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('SCHOOL_ADMIN','GOVERNMENT_AUDITOR','COMPLIANCE_OFFICER')")
    public ResponseEntity<?> getLogsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(auditLogService.getLogsByUser(userId));
    }
}
