package com.educonnect.service.impl;

import com.educonnect.dto.response.ProgressResponse;
import com.educonnect.entity.Progress;
import com.educonnect.exception.ResourceNotFoundException;
import com.educonnect.repository.ProgressRepository;
import com.educonnect.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProgressService {

    private final ProgressRepository progressRepository;
    private final StudentRepository studentRepository;

    public List<ProgressResponse> getProgressByStudent(Long studentId) {
        return progressRepository.findByStudent_StudentId(studentId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public ProgressResponse getProgressByStudentAndCourse(Long studentId, Long courseId) {
        Progress progress = progressRepository
                .findByStudent_StudentIdAndCourse_CourseId(studentId, courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Progress not found for student/course"));
        return toResponse(progress);
    }

    public List<ProgressResponse> getProgressByCourse(Long courseId) {
        return progressRepository.findByCourse_CourseId(courseId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    private ProgressResponse toResponse(Progress p) {
        return ProgressResponse.builder()
                .progressId(p.getProgressId())
                .studentId(p.getStudent().getStudentId())
                .studentName(p.getStudent().getName())
                .courseId(p.getCourse().getCourseId())
                .courseTitle(p.getCourse().getTitle())
                .completionPercentage(p.getCompletionPercentage())
                .metricsJson(p.getMetricsJson())
                .date(p.getDate())
                .build();
    }
}
