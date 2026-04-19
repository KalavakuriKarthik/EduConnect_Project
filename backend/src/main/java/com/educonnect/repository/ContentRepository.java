package com.educonnect.repository;
import com.educonnect.entity.Content;
import com.educonnect.enums.ContentType;
import com.educonnect.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {
    List<Content> findByCourse_CourseId(Long courseId);
    List<Content> findByCourse_CourseIdAndStatus(Long courseId, Status status);
    List<Content> findByType(ContentType type);
}
