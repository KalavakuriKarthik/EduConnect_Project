package com.educonnect.repository;
import com.educonnect.entity.Progress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository
public interface ProgressRepository extends JpaRepository<Progress, Long> {
    List<Progress> findByStudent_StudentId(Long studentId);
    Optional<Progress> findByStudent_StudentIdAndCourse_CourseId(Long studentId, Long courseId);
    List<Progress> findByCourse_CourseId(Long courseId);
}
