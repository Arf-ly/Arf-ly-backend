package com.capstone.arfly.walkRecord.repository;

import com.capstone.arfly.walkRecord.domain.WalkRecord;
import com.capstone.arfly.pet.domain.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WalkRecordRepository extends JpaRepository<WalkRecord, Long> {

    // 1. 특정 회원의 '미배정(status = false)' 산책 기록 목록 조회
    // (홈 화면 및 미배정 알림에 사용)
    List<WalkRecord> findByMemberIdAndStatusFalse(Long memberId);
    // 미배정 산책 기록
    List<WalkRecord> findAllByMember_IdAndPetIsNullOrderByCreatedAtDesc(Long memberId);

    // 배정된 산책 기록 전체
    @Query("""
        SELECT w FROM WalkRecord w
        JOIN FETCH w.pet p
        LEFT JOIN FETCH p.profileImage
        JOIN FETCH p.breeds
        WHERE w.member.id = :memberId
        ORDER BY w.createdAt DESC
        """)
    List<WalkRecord> findAllAssignedByMemberId(@Param("memberId") Long memberId);

    // 2. 특정 펫에 '배정완료(status = true)'된 모든 산책 기록 조회
    // (반려동물별 전체 산책 이력)
    List<WalkRecord> findByPetIdAndStatusTrue(Long petId);
    // 배정된 산책 기록 — 특정 pet 필터
    @Query("""
        SELECT w FROM WalkRecord w
        JOIN FETCH w.pet p
        LEFT JOIN FETCH p.profileImage
        JOIN FETCH p.breeds
        WHERE w.member.id = :memberId AND p.id = :petId
        ORDER BY w.createdAt DESC
        """)
    List<WalkRecord> findAllByMemberIdAndPetId(@Param("memberId") Long memberId, @Param("petId") Long petId);

    // 3. 특정 펫의 특정 기간(주간/월간) 산책 기록 조회
    // (주간 활동량 통계 API에 사용)
    List<WalkRecord> findByPetIdAndStatusTrueAndStartTimeBetween(Long petId, LocalDateTime start, LocalDateTime end);
    // 특정 pet의 기간 내 산책 기록
    List<WalkRecord> findAllByPet_IdAndStartTimeBetween(Long petId, LocalDateTime start, LocalDateTime end);

    void deleteAllByPet(Pet pet);
}
