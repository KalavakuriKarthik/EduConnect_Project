package com.educonnect.repository;
import com.educonnect.entity.StudentDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface StudentDocumentRepository extends JpaRepository<StudentDocument, Long> {
    List<StudentDocument> findByStudent_StudentId(Long studentId);
    List<StudentDocument> findByVerificationStatus(String status);
}
