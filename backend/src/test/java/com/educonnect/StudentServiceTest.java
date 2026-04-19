package com.educonnect;

import com.educonnect.dto.request.StudentProfileRequest;
import com.educonnect.dto.response.StudentResponse;
import com.educonnect.entity.Student;
import com.educonnect.entity.User;
import com.educonnect.enums.Role;
import com.educonnect.enums.Status;
import com.educonnect.exception.BadRequestException;
import com.educonnect.repository.StudentRepository;
import com.educonnect.repository.UserRepository;
import com.educonnect.service.impl.StudentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock private StudentRepository studentRepository;
    @Mock private UserRepository userRepository;
    @InjectMocks private StudentService studentService;

    @Test
    void createProfile_success() {
        User user = User.builder().userId(1L).name("Bob").email("bob@edu.com")
                .role(Role.STUDENT).status(Status.ACTIVE).build();
        Student student = Student.builder().studentId(1L).user(user).name("Bob").status(Status.ACTIVE).build();

        when(studentRepository.existsByUser_UserId(1L)).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(studentRepository.save(any())).thenReturn(student);

        StudentProfileRequest req = new StudentProfileRequest();
        req.setName("Bob");

        StudentResponse response = studentService.createProfile(1L, req);
        assertNotNull(response);
        assertEquals("Bob", response.getName());
    }

    @Test
    void createProfile_alreadyExists_throwsException() {
        when(studentRepository.existsByUser_UserId(1L)).thenReturn(true);
        assertThrows(BadRequestException.class,
                () -> studentService.createProfile(1L, new StudentProfileRequest()));
    }
}
