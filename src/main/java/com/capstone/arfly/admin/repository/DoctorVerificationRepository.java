package com.capstone.arfly.admin.repository;

import com.capstone.arfly.admin.domain.DoctorVerification;
import com.capstone.arfly.admin.domain.VerificationStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorVerificationRepository extends JpaRepository<DoctorVerification, Long> {

    boolean existsByMemberIdAndStatus(Long memberId, VerificationStatus status);

    @EntityGraph(attributePaths = "member")
    List<DoctorVerification> findAllByStatusOrderByCreatedAtAsc(VerificationStatus status);

    @EntityGraph(attributePaths = {"member", "licenseImage"})
    Optional<DoctorVerification> findWithMemberAndLicenseImageById(Long id);
}
