package com.educonnect.repository;
import com.educonnect.entity.Notification;
import com.educonnect.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUser_UserId(Long userId);
    List<Notification> findByUser_UserIdAndStatus(Long userId, Status status);
    long countByUser_UserIdAndStatus(Long userId, Status status);
}
