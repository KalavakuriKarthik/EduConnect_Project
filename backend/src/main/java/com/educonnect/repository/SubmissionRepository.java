package com.educonnect.repository;
import com.educonnect.entity.Submission;
import com.educonnect.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    List<Submission> findByStudent_StudentId(Long studentId);
    List<Submission> findByAssessment_AssessmentId(Long assessmentId);
    Optional<Submission> findByAssessment_AssessmentIdAndStudent_StudentId(Long a, Long s);
    List<Submission> findByStatus(Status status);
}
