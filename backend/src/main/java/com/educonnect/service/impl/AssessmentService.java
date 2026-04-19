package com.educonnect.service.impl;

import com.educonnect.dto.request.AssessmentRequest;
import com.educonnect.dto.request.GradeRequest;
import com.educonnect.dto.request.SubmissionRequest;
import com.educonnect.dto.response.AssessmentResponse;
import com.educonnect.dto.response.SubmissionResponse;
import com.educonnect.entity.*;
import com.educonnect.enums.NotificationCategory;
import com.educonnect.enums.Status;
import com.educonnect.exception.BadRequestException;
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
public class AssessmentService {

    private static final Logger logger = LoggerFactory.getLogger(AssessmentService.class);

    private final AssessmentRepository assessmentRepository;
    private final SubmissionRepository submissionRepository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final NotificationRepository notificationRepository;
    private final ProgressRepository progressRepository;

    @Transactional
    public AssessmentResponse createAssessment(AssessmentRequest request) {
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course", request.getCourseId()));
        Assessment assessment = Assessment.builder()
                .course(course)
                .title(request.getTitle())
                .type(request.getType())
                .date(request.getDate())
                .description(request.getDescription())
                .status(Status.ACTIVE)
                .build();
        assessment = assessmentRepository.save(assessment);
        logger.info("Assessment created: {}", assessment.getTitle());
        return toResponse(assessment);
    }

    public List<AssessmentResponse> getAssessmentsByCourse(Long courseId) {
        return assessmentRepository.findByCourse_CourseId(courseId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public AssessmentResponse getAssessmentById(Long id) {
        return toResponse(assessmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment", id)));
    }

    public List<AssessmentResponse> getAllAssessments() {
        return assessmentRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public SubmissionResponse submitAssessment(SubmissionRequest request) {
        Assessment assessment = assessmentRepository.findById(request.getAssessmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Assessment", request.getAssessmentId()));
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student", request.getStudentId()));

        submissionRepository.findByAssessment_AssessmentIdAndStudent_StudentId(
                request.getAssessmentId(), request.getStudentId())
                .ifPresent(s -> { throw new BadRequestException("Already submitted this assessment"); });

        Submission submission = Submission.builder()
                .assessment(assessment)
                .student(student)
                .fileUri(request.getFileUri())
                .status(Status.PENDING)
                .build();
        submission = submissionRepository.save(submission);

        // Notify teacher
        if (assessment.getCourse().getTeacher() != null) {
            Notification notification = Notification.builder()
                    .user(assessment.getCourse().getTeacher())
                    .entityId(submission.getSubmissionId())
                    .message(student.getName() + " submitted: " + assessment.getTitle())
                    .category(NotificationCategory.ASSESSMENT)
                    .build();
            notificationRepository.save(notification);
        }
        logger.info("Submission saved for student {} assessment {}", student.getStudentId(), assessment.getAssessmentId());
        return toSubmissionResponse(submission);
    }

    @Transactional
    public SubmissionResponse gradeSubmission(Long submissionId, GradeRequest request) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Submission", submissionId));
        submission.setGrade(request.getGrade());
        submission.setFeedback(request.getFeedback());
        submission.setStatus(Status.COMPLETED);
        submission = submissionRepository.save(submission);

        // Update progress
        updateProgress(submission.getStudent().getStudentId(),
                submission.getAssessment().getCourse().getCourseId());

        // Notify student
        Notification notification = Notification.builder()
                .user(submission.getStudent().getUser())
                .entityId(submissionId)
                .message("Your submission for '" + submission.getAssessment().getTitle()
                        + "' has been graded. Grade: " + request.getGrade())
                .category(NotificationCategory.ASSESSMENT)
                .build();
        notificationRepository.save(notification);

        return toSubmissionResponse(submission);
    }

    public List<SubmissionResponse> getSubmissionsByStudent(Long studentId) {
        return submissionRepository.findByStudent_StudentId(studentId)
                .stream().map(this::toSubmissionResponse).collect(Collectors.toList());
    }

    public List<SubmissionResponse> getSubmissionsByAssessment(Long assessmentId) {
        return submissionRepository.findByAssessment_AssessmentId(assessmentId)
                .stream().map(this::toSubmissionResponse).collect(Collectors.toList());
    }

    private void updateProgress(Long studentId, Long courseId) {
        Progress progress = progressRepository
                .findByStudent_StudentIdAndCourse_CourseId(studentId, courseId)
                .orElse(null);

        List<Submission> graded = submissionRepository.findByStudent_StudentId(studentId)
                .stream()
                .filter(s -> s.getAssessment().getCourse().getCourseId().equals(courseId))
                .filter(s -> s.getStatus() == Status.COMPLETED)
                .collect(Collectors.toList());

        List<Assessment> all = assessmentRepository.findByCourse_CourseId(courseId);
        double pct = all.isEmpty() ? 0 : (graded.size() * 100.0 / all.size());

        if (progress == null) {
            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Student", studentId));
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new ResourceNotFoundException("Course", courseId));
            progress = Progress.builder().student(student).course(course).build();
        }
        progress.setCompletionPercentage(pct);
        progress.setMetricsJson("{\"gradedSubmissions\":" + graded.size() + ",\"totalAssessments\":" + all.size() + "}");
        progressRepository.save(progress);
    }

    private AssessmentResponse toResponse(Assessment a) {
        return AssessmentResponse.builder()
                .assessmentId(a.getAssessmentId())
                .title(a.getTitle())
                .type(a.getType())
                .courseId(a.getCourse().getCourseId())
                .courseTitle(a.getCourse().getTitle())
                .date(a.getDate())
                .description(a.getDescription())
                .status(a.getStatus())
                .submissionCount(submissionRepository.findByAssessment_AssessmentId(a.getAssessmentId()).size())
                .build();
    }

    private SubmissionResponse toSubmissionResponse(Submission s) {
        return SubmissionResponse.builder()
                .submissionId(s.getSubmissionId())
                .assessmentId(s.getAssessment().getAssessmentId())
                .assessmentTitle(s.getAssessment().getTitle())
                .studentId(s.getStudent().getStudentId())
                .studentName(s.getStudent().getName())
                .fileUri(s.getFileUri())
                .grade(s.getGrade())
                .feedback(s.getFeedback())
                .date(s.getDate())
                .status(s.getStatus())
                .build();
    }
}
