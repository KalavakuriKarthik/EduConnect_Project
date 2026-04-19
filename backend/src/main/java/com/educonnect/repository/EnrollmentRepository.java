package com.educonnect.repository;
import com.educonnect.entity.Enrollment;
import com.educonnect.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByStudent_StudentId(Long studentId);
    List<Enrollment> findByCourse_CourseId(Long courseId);
    Optional<Enrollment> findByStudent_StudentIdAndCourse_CourseId(Long studentId, Long courseId);
    boolean existsByStudent_StudentIdAndCourse_CourseId(Long studentId, Long courseId);
    long countByCourse_CourseId(Long courseId);
    List<Enrollment> findByStatus(Status status);
}
