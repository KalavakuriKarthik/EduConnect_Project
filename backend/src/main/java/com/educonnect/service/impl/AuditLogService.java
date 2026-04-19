package com.educonnect.service.impl;

import com.educonnect.entity.AuditLog;
import com.educonnect.entity.User;
import com.educonnect.repository.AuditLogRepository;
import com.educonnect.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;

    public void log(Long userId, String action, String resource) {
        userRepository.findById(userId).ifPresent(user -> {
            AuditLog log = AuditLog.builder()
                    .user(user).action(action).resource(resource).build();
            auditLogRepository.save(log);
        });
    }

    public List<AuditLog> getAllLogs() {
        return auditLogRepository.findAll();
    }

    public List<AuditLog> getLogsByUser(Long userId) {
        return auditLogRepository.findByUser_UserId(userId);
    }
}
