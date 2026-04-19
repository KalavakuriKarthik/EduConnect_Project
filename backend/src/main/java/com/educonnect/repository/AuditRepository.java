package com.educonnect.repository;
import com.educonnect.entity.Audit;
import com.educonnect.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface AuditRepository extends JpaRepository<Audit, Long> {
    List<Audit> findByOfficer_UserId(Long officerId);
    List<Audit> findByStatus(Status status);
}
