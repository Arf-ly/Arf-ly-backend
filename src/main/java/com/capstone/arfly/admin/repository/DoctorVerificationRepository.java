package com.capstone.arfly.admin.repository;

import com.capstone.arfly.admin.domain.DoctorVerification;
import com.capstone.arfly.admin.domain.VerificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorVerificationRepository extends JpaRepository<DoctorVerification, Long> {

    // 심사 대기 중인 신청이 이미 있는지 (중복 신청 방지)
    boolean existsByMemberIdAndStatus(Long memberId, VerificationStatus status);
}
