package com.educonnect.service.impl;

import com.educonnect.dto.request.StudentProfileRequest;
import com.educonnect.dto.response.StudentResponse;
import com.educonnect.entity.Student;
import com.educonnect.entity.User;
import com.educonnect.exception.BadRequestException;
import com.educonnect.exception.ResourceNotFoundException;
import com.educonnect.repository.StudentRepository;
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
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    @Transactional
    public StudentResponse createProfile(Long userId, StudentProfileRequest request) {
        if (studentRepository.existsByUser_UserId(userId)) {
            throw new BadRequestException("Student profile already exists for user: " + userId);
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        Student student = Student.builder()
                .user(user)
                .name(request.getName() != null ? request.getName() : user.getName())
                .dob(request.getDob())
                .gender(request.getGender())
                .address(request.getAddress())
                .contactInfo(request.getContactInfo())
                .build();
        student = studentRepository.save(student);
        logger.info("Student profile created for user: {}", userId);
        return toResponse(student);
    }

    public StudentResponse getStudentByUserId(Long userId) {
        Student student = studentRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Student profile not found for user: " + userId));
        return toResponse(student);
    }

    public StudentResponse getStudentById(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", studentId));
        return toResponse(student);
    }

    public List<StudentResponse> getAllStudents() {
        return studentRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public StudentResponse updateProfile(Long studentId, StudentProfileRequest request) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", studentId));
        if (request.getName() != null) student.setName(request.getName());
        if (request.getDob() != null) student.setDob(request.getDob());
        if (request.getGender() != null) student.setGender(request.getGender());
        if (request.getAddress() != null) student.setAddress(request.getAddress());
        if (request.getContactInfo() != null) student.setContactInfo(request.getContactInfo());
        return toResponse(studentRepository.save(student));
    }

    private StudentResponse toResponse(Student s) {
        return StudentResponse.builder()
                .studentId(s.getStudentId())
                .userId(s.getUser() != null ? s.getUser().getUserId() : null)
                .name(s.getName())
                .email(s.getUser() != null ? s.getUser().getEmail() : null)
                .dob(s.getDob())
                .gender(s.getGender())
                .address(s.getAddress())
                .contactInfo(s.getContactInfo())
                .status(s.getStatus())
                .build();
    }
}
