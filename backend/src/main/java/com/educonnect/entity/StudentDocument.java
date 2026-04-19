package com.educonnect.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "student_documents")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long documentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(nullable = false)
    private String docType; // IDProof / Transcript

    @Column(nullable = false)
    private String fileUri;

    @Builder.Default
    private LocalDateTime uploadedDate = LocalDateTime.now();

    @Builder.Default
    private String verificationStatus = "PENDING"; // PENDING / VERIFIED / REJECTED
}
