package com.educonnect.repository;
import com.educonnect.entity.ContentAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface ContentAccessRepository extends JpaRepository<ContentAccess, Long> {
    List<ContentAccess> findByStudent_StudentId(Long studentId);
    List<ContentAccess> findByContent_ContentId(Long contentId);
    boolean existsByStudent_StudentIdAndContent_ContentId(Long studentId, Long contentId);
    long countByContent_ContentId(Long contentId);
}
