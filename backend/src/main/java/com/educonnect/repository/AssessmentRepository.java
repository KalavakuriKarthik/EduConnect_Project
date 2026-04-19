package com.educonnect.repository;
import com.educonnect.entity.Assessment;
import com.educonnect.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, Long> {
    List<Assessment> findByCourse_CourseId(Long courseId);
    List<Assessment> findByStatus(Status status);
}
