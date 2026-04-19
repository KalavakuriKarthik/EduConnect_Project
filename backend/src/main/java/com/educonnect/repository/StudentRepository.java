package com.educonnect.repository;
import com.educonnect.entity.Student;
import com.educonnect.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUser_UserId(Long userId);
    List<Student> findByStatus(Status status);
    boolean existsByUser_UserId(Long userId);
}
