package com.educonnect.service.impl;

import com.educonnect.dto.request.ContentRequest;
import com.educonnect.dto.response.ContentResponse;
import com.educonnect.entity.*;
import com.educonnect.enums.Status;
import com.educonnect.exception.ResourceNotFoundException;
import com.educonnect.repository.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContentService {

    private static final Logger logger = LoggerFactory.getLogger(ContentService.class);

    private final ContentRepository contentRepository;
    private final CourseRepository courseRepository;
    private final ContentAccessRepository contentAccessRepository;
    private final StudentRepository studentRepository;

    @Transactional
    public ContentResponse uploadContent(ContentRequest request) {
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course", request.getCourseId()));
        Content content = Content.builder()
                .course(course)
                .title(request.getTitle())
                .type(request.getType())
                .fileUri(request.getFileUri())
                .status(Status.ACTIVE)
                .build();
        content = contentRepository.save(content);
        logger.info("Content uploaded: {} for course {}", content.getTitle(), course.getCourseId());
        return toResponse(content);
    }

    public List<ContentResponse> getContentByCourse(Long courseId) {
        return contentRepository.findByCourse_CourseId(courseId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public ContentResponse getContentById(Long contentId) {
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new ResourceNotFoundException("Content", contentId));
        return toResponse(content);
    }

    @Transactional
    public void trackAccess(Long studentId, Long contentId) {
        if (!contentAccessRepository.existsByStudent_StudentIdAndContent_ContentId(studentId, contentId)) {
            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Student", studentId));
            Content content = contentRepository.findById(contentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Content", contentId));
            ContentAccess access = ContentAccess.builder()
                    .student(student).content(content).status(Status.ACTIVE).build();
            contentAccessRepository.save(access);
        }
    }

    @Transactional
    public void deleteContent(Long contentId) {
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new ResourceNotFoundException("Content", contentId));
        content.setStatus(Status.INACTIVE);
        contentRepository.save(content);
    }

    private ContentResponse toResponse(Content c) {
        return ContentResponse.builder()
                .contentId(c.getContentId())
                .courseId(c.getCourse().getCourseId())
                .courseTitle(c.getCourse().getTitle())
                .title(c.getTitle())
                .type(c.getType())
                .fileUri(c.getFileUri())
                .uploadedDate(c.getUploadedDate())
                .status(c.getStatus())
                .accessCount(contentAccessRepository.countByContent_ContentId(c.getContentId()))
                .build();
    }
}
