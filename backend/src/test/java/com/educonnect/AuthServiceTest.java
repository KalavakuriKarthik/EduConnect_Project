package com.educonnect;

import com.educonnect.dto.request.RegisterRequest;
import com.educonnect.dto.response.AuthResponse;
import com.educonnect.entity.User;
import com.educonnect.enums.Role;
import com.educonnect.enums.Status;
import com.educonnect.exception.BadRequestException;
import com.educonnect.repository.UserRepository;
import com.educonnect.security.JwtUtils;
import com.educonnect.service.impl.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private JwtUtils jwtUtils;
    @InjectMocks private AuthService authService;

    @Test
    void register_success() {
        RegisterRequest request = new RegisterRequest();
        request.setName("Alice"); request.setEmail("alice@edu.com");
        request.setPassword("password123"); request.setRole(Role.STUDENT);

        User saved = User.builder().userId(1L).name("Alice").email("alice@edu.com")
                .role(Role.STUDENT).status(Status.ACTIVE).build();

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hashed");
        when(userRepository.save(any())).thenReturn(saved);
        when(jwtUtils.generateToken(any())).thenReturn("mock-jwt-token");

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("alice@edu.com", response.getEmail());
        assertEquals("mock-jwt-token", response.getToken());
    }

    @Test
    void register_duplicateEmail_throwsException() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("alice@edu.com");
        when(userRepository.existsByEmail("alice@edu.com")).thenReturn(true);
        assertThrows(BadRequestException.class, () -> authService.register(request));
    }
}
