package com.educonnect.controller;

import com.educonnect.dto.request.ContentRequest;
import com.educonnect.service.impl.ContentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/content")
@RequiredArgsConstructor
public class ContentController {

    private final ContentService contentService;

    @PostMapping
    @PreAuthorize("hasAnyRole('TEACHER','SCHOOL_ADMIN')")
    public ResponseEntity<?> uploadContent(@Valid @RequestBody ContentRequest request) {
        return ResponseEntity.ok(contentService.uploadContent(request));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<?> getContentByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(contentService.getContentByCourse(courseId));
    }

    @GetMapping("/{contentId}")
    public ResponseEntity<?> getContentById(@PathVariable Long contentId) {
        return ResponseEntity.ok(contentService.getContentById(contentId));
    }

    @PostMapping("/{contentId}/access/{studentId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> trackAccess(@PathVariable Long contentId, @PathVariable Long studentId) {
        contentService.trackAccess(studentId, contentId);
        return ResponseEntity.ok("Access recorded");
    }

    @DeleteMapping("/{contentId}")
    @PreAuthorize("hasAnyRole('TEACHER','SCHOOL_ADMIN')")
    public ResponseEntity<?> deleteContent(@PathVariable Long contentId) {
        contentService.deleteContent(contentId);
        return ResponseEntity.ok("Content removed");
    }
}
