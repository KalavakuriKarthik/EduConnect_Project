package com.educonnect;

import com.educonnect.dto.request.CourseRequest;
import com.educonnect.dto.response.CourseResponse;
import com.educonnect.entity.Course;
import com.educonnect.entity.User;
import com.educonnect.enums.Role;
import com.educonnect.enums.Status;
import com.educonnect.exception.ResourceNotFoundException;
import com.educonnect.repository.CourseRepository;
import com.educonnect.repository.EnrollmentRepository;
import com.educonnect.repository.UserRepository;
import com.educonnect.service.impl.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock private CourseRepository courseRepository;
    @Mock private UserRepository userRepository;
    @Mock private EnrollmentRepository enrollmentRepository;
    @InjectMocks private CourseService courseService;

    private User teacher;
    private Course course;

    @BeforeEach
    void setUp() {
        teacher = User.builder().userId(1L).name("John Teacher")
                .email("teacher@edu.com").role(Role.TEACHER).status(Status.ACTIVE).build();
        course = Course.builder().courseId(1L).title("Java Basics")
                .teacher(teacher).status(Status.ACTIVE).build();
    }

    @Test
    void createCourse_success() {
        CourseRequest request = new CourseRequest();
        request.setTitle("Java Basics");
        request.setTeacherId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(teacher));
        when(courseRepository.save(any())).thenReturn(course);
        when(enrollmentRepository.countByCourse_CourseId(any())).thenReturn(0L);

        CourseResponse response = courseService.createCourse(request);

        assertNotNull(response);
        assertEquals("Java Basics", response.getTitle());
        verify(courseRepository, times(1)).save(any());
    }

    @Test
    void getCourseById_notFound_throwsException() {
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> courseService.getCourseById(99L));
    }

    @Test
    void getAllCourses_returnsList() {
        when(courseRepository.findAll()).thenReturn(List.of(course));
        when(enrollmentRepository.countByCourse_CourseId(any())).thenReturn(5L);
        List<CourseResponse> courses = courseService.getAllCourses();
        assertFalse(courses.isEmpty());
        assertEquals(1, courses.size());
    }

    @Test
    void deleteCourse_success() {
        when(courseRepository.existsById(1L)).thenReturn(true);
        doNothing().when(courseRepository).deleteById(1L);
        assertDoesNotThrow(() -> courseService.deleteCourse(1L));
        verify(courseRepository, times(1)).deleteById(1L);
    }
}
