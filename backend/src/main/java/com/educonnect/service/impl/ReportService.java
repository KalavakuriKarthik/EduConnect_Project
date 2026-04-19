package com.educonnect.service.impl;

import com.educonnect.entity.Report;
import com.educonnect.entity.User;
import com.educonnect.exception.ResourceNotFoundException;
import com.educonnect.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final SubmissionRepository submissionRepository;
    private final ProgressRepository progressRepository;
    private final ComplianceRecordRepository complianceRecordRepository;
    private final StudentRepository studentRepository;
    private final AssessmentRepository assessmentRepository;

    public Map<String, Object> getDashboardMetrics() {
        long totalStudents = studentRepository.count();
        long totalCourses = courseRepository.count();
        long totalEnrollments = enrollmentRepository.count();
        long totalAssessments = assessmentRepository.count();
        long totalSubmissions = submissionRepository.count();
        long complianceRecords = complianceRecordRepository.count();

        double avgCompletion = progressRepository.findAll().stream()
                .filter(p -> p.getCompletionPercentage() != null)
                .mapToDouble(p -> p.getCompletionPercentage())
                .average().orElse(0.0);

        return Map.of(
                "totalStudents", totalStudents,
                "totalCourses", totalCourses,
                "totalEnrollments", totalEnrollments,
                "totalAssessments", totalAssessments,
                "totalSubmissions", totalSubmissions,
                "complianceRecords", complianceRecords,
                "averageCourseCompletion", Math.round(avgCompletion * 100.0) / 100.0
        );
    }

    public Map<String, Object> getCourseReport(Long courseId) {
        var course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", courseId));
        long enrollments = enrollmentRepository.countByCourse_CourseId(courseId);
        var progressList = progressRepository.findByCourse_CourseId(courseId);
        double avgCompletion = progressList.stream()
                .filter(p -> p.getCompletionPercentage() != null)
                .mapToDouble(p -> p.getCompletionPercentage())
                .average().orElse(0.0);
        var assessments = assessmentRepository.findByCourse_CourseId(courseId);

        return Map.of(
                "courseId", courseId,
                "courseTitle", course.getTitle(),
                "totalEnrollments", enrollments,
                "totalAssessments", assessments.size(),
                "averageCompletion", Math.round(avgCompletion * 100.0) / 100.0
        );
    }

    @Transactional
    public Report generateReport(String scope, Long generatedByUserId) {
        User generatedBy = generatedByUserId != null
                ? userRepository.findById(generatedByUserId).orElse(null)
                : null;
        Map<String, Object> metrics = getDashboardMetrics();
        String metricsJson = metrics.toString();
        Report report = Report.builder()
                .scope(scope)
                .metrics(metricsJson)
                .generatedBy(generatedBy)
                .build();
        return reportRepository.save(report);
    }

    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    public List<Report> getReportsByScope(String scope) {
        return reportRepository.findByScope(scope);
    }
}
