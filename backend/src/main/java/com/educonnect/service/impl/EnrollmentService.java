package com.educonnect.service.impl;

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
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private static final Logger logger = LoggerFactory.getLogger(EnrollmentService.class);

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Transactional
    public Map<String, Object> enroll(Long studentId, Long courseId) {
        if (enrollmentRepository.existsByStudent_StudentIdAndCourse_CourseId(studentId, courseId)) {
            throw new BadRequestException("Student already enrolled in this course");
        }
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", studentId));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", courseId));

        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .course(course)
                .status(Status.ACTIVE)
                .build();
        enrollmentRepository.save(enrollment);

        // Send notification
        Notification notification = Notification.builder()
                .user(student.getUser())
                .entityId(courseId)
                .message("You have been enrolled in: " + course.getTitle())
                .category(NotificationCategory.COURSE)
                .build();
        notificationRepository.save(notification);

        logger.info("Student {} enrolled in course {}", studentId, courseId);
        return Map.of("message", "Enrolled successfully", "courseTitle", course.getTitle(), "studentName", student.getName());
    }

    @Transactional
    public void unenroll(Long studentId, Long courseId) {
        Enrollment enrollment = enrollmentRepository
                .findByStudent_StudentIdAndCourse_CourseId(studentId, courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));
        enrollment.setStatus(Status.CANCELLED);
        enrollmentRepository.save(enrollment);
    }

    public List<Map<String, Object>> getEnrollmentsByStudent(Long studentId) {
        return enrollmentRepository.findByStudent_StudentId(studentId).stream()
                .map(e -> Map.<String, Object>of(
                        "enrollmentId", e.getEnrollmentId(),
                        "courseId", e.getCourse().getCourseId(),
                        "courseTitle", e.getCourse().getTitle(),
                        "status", e.getStatus(),
                        "date", e.getDate()
                )).collect(Collectors.toList());
    }

    public List<Map<String, Object>> getEnrollmentsByCourse(Long courseId) {
        return enrollmentRepository.findByCourse_CourseId(courseId).stream()
                .map(e -> Map.<String, Object>of(
                        "enrollmentId", e.getEnrollmentId(),
                        "studentId", e.getStudent().getStudentId(),
                        "studentName", e.getStudent().getName(),
                        "status", e.getStatus(),
                        "date", e.getDate()
                )).collect(Collectors.toList());
    }
}
