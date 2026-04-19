package com.educonnect.repository;
import com.educonnect.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByUser_UserId(Long userId);
    List<AuditLog> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
}
