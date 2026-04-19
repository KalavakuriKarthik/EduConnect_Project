package com.educonnect.service.impl;

import com.educonnect.dto.request.AuditRequest;
import com.educonnect.dto.request.ComplianceRecordRequest;
import com.educonnect.entity.Audit;
import com.educonnect.entity.ComplianceRecord;
import com.educonnect.entity.User;
import com.educonnect.enums.Status;
import com.educonnect.exception.ResourceNotFoundException;
import com.educonnect.repository.AuditRepository;
import com.educonnect.repository.ComplianceRecordRepository;
import com.educonnect.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ComplianceService {

    private static final Logger logger = LoggerFactory.getLogger(ComplianceService.class);

    private final ComplianceRecordRepository complianceRecordRepository;
    private final AuditRepository auditRepository;
    private final UserRepository userRepository;

    @Transactional
    public ComplianceRecord createComplianceRecord(ComplianceRecordRequest request) {
        ComplianceRecord record = ComplianceRecord.builder()
                .entityId(request.getEntityId())
                .type(request.getType())
                .result(request.getResult())
                .notes(request.getNotes())
                .status(Status.ACTIVE)
                .build();
        logger.info("Compliance record created: type={}, result={}", request.getType(), request.getResult());
        return complianceRecordRepository.save(record);
    }

    public List<ComplianceRecord> getAllComplianceRecords() {
        return complianceRecordRepository.findAll();
    }

    public List<ComplianceRecord> getRecordsByType(String type) {
        return complianceRecordRepository.findByType(type);
    }

    public List<ComplianceRecord> getRecordsByResult(String result) {
        return complianceRecordRepository.findByResult(result);
    }

    @Transactional
    public Audit createAudit(Long officerId, AuditRequest request) {
        User officer = userRepository.findById(officerId)
                .orElseThrow(() -> new ResourceNotFoundException("User", officerId));
        Audit audit = Audit.builder()
                .officer(officer)
                .scope(request.getScope())
                .findings(request.getFindings())
                .status(Status.PENDING)
                .build();
        logger.info("Audit created by officer {}: scope={}", officerId, request.getScope());
        return auditRepository.save(audit);
    }

    public List<Audit> getAllAudits() {
        return auditRepository.findAll();
    }

    public List<Audit> getAuditsByOfficer(Long officerId) {
        return auditRepository.findByOfficer_UserId(officerId);
    }

    @Transactional
    public Audit updateAuditStatus(Long auditId, String status) {
        Audit audit = auditRepository.findById(auditId)
                .orElseThrow(() -> new ResourceNotFoundException("Audit", auditId));
        audit.setStatus(Status.valueOf(status));
        return auditRepository.save(audit);
    }

    public Map<String, Object> getComplianceSummary() {
        List<ComplianceRecord> all = complianceRecordRepository.findAll();
        long compliant = all.stream().filter(r -> "COMPLIANT".equals(r.getResult())).count();
        long nonCompliant = all.stream().filter(r -> "NON_COMPLIANT".equals(r.getResult())).count();
        long underReview = all.stream().filter(r -> "UNDER_REVIEW".equals(r.getResult())).count();
        return Map.of(
                "total", all.size(),
                "compliant", compliant,
                "nonCompliant", nonCompliant,
                "underReview", underReview,
                "complianceRate", all.isEmpty() ? 0 : (compliant * 100.0 / all.size())
        );
    }
}
