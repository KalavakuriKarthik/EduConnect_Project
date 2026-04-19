package com.educonnect.repository;
import com.educonnect.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByScope(String scope);
    List<Report> findByGeneratedBy_UserId(Long userId);
}
