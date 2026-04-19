package com.educonnect.service.impl;

import com.educonnect.dto.request.CourseRequest;
import com.educonnect.dto.response.CourseResponse;
import com.educonnect.entity.Course;
import com.educonnect.entity.User;
import com.educonnect.enums.Status;
import com.educonnect.exception.ResourceNotFoundException;
import com.educonnect.repository.CourseRepository;
import com.educonnect.repository.EnrollmentRepository;
import com.educonnect.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {

    private static final Logger logger = LoggerFactory.getLogger(CourseService.class);

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Transactional
    public CourseResponse createCourse(CourseRequest request) {
        User teacher = null;
        if (request.getTeacherId() != null) {
            teacher = userRepository.findById(request.getTeacherId())
                    .orElseThrow(() -> new ResourceNotFoundException("Teacher", request.getTeacherId()));
        }
        Course course = Course.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .teacher(teacher)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status(Status.ACTIVE)
                .build();
        course = courseRepository.save(course);
        logger.info("Course created: {}", course.getTitle());
        return toResponse(course);
    }

    public List<CourseResponse> getAllCourses() {
        return courseRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<CourseResponse> getCoursesByTeacher(Long teacherId) {
        return courseRepository.findByTeacher_UserId(teacherId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    public CourseResponse getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", id));
        return toResponse(course);
    }

    @Transactional
    public CourseResponse updateCourse(Long id, CourseRequest request) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", id));
        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setStartDate(request.getStartDate());
        course.setEndDate(request.getEndDate());
        if (request.getTeacherId() != null) {
            User teacher = userRepository.findById(request.getTeacherId())
                    .orElseThrow(() -> new ResourceNotFoundException("Teacher", request.getTeacherId()));
            course.setTeacher(teacher);
        }
        return toResponse(courseRepository.save(course));
    }

    @Transactional
    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) throw new ResourceNotFoundException("Course", id);
        courseRepository.deleteById(id);
        logger.info("Course deleted: {}", id);
    }

    public List<CourseResponse> searchCourses(String title) {
        return courseRepository.findByTitleContainingIgnoreCase(title).stream().map(this::toResponse).collect(Collectors.toList());
    }

    private CourseResponse toResponse(Course c) {
        return CourseResponse.builder()
                .courseId(c.getCourseId())
                .title(c.getTitle())
                .description(c.getDescription())
                .teacherId(c.getTeacher() != null ? c.getTeacher().getUserId() : null)
                .teacherName(c.getTeacher() != null ? c.getTeacher().getName() : null)
                .startDate(c.getStartDate())
                .endDate(c.getEndDate())
                .status(c.getStatus())
                .enrollmentCount(enrollmentRepository.countByCourse_CourseId(c.getCourseId()))
                .createdAt(c.getCreatedAt())
                .build();
    }
}
