package com.educonnect.repository;
import com.educonnect.entity.Course;
import com.educonnect.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByTeacher_UserId(Long teacherId);
    List<Course> findByStatus(Status status);
    List<Course> findByTitleContainingIgnoreCase(String title);
}
