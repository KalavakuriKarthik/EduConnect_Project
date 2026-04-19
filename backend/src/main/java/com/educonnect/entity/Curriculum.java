package com.educonnect.entity;

import com.educonnect.enums.Status;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "curricula")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Curriculum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long curriculumId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(columnDefinition = "JSON")
    private String topicsJson;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Status status = Status.ACTIVE;
}
