package com.educonnect.repository;
import com.educonnect.entity.ComplianceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface ComplianceRecordRepository extends JpaRepository<ComplianceRecord, Long> {
    List<ComplianceRecord> findByType(String type);
    List<ComplianceRecord> findByResult(String result);
    List<ComplianceRecord> findByEntityId(Long entityId);
}
