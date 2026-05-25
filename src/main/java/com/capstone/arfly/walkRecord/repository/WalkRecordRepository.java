package com.capstone.arfly.walkRecord.repository;

import com.capstone.arfly.walkRecord.domain.WalkRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WalkRecordRepository extends JpaRepository<WalkRecord, Long> {

    // 미배정 산책 기록
    List<WalkRecord> findAllByMember_IdAndPetIsNull(Long memberId);

    // 배정된 산책 기록 전체
    @Query("""
        SELECT w FROM WalkRecord w
        JOIN FETCH w.pet p
        LEFT JOIN FETCH p.profileImage
        JOIN FETCH p.breeds
        WHERE w.member.id = :memberId
        """)
    List<WalkRecord> findAllAssignedByMemberId(@Param("memberId") Long memberId);

    // 배정된 산책 기록 — 특정 pet 필터
    @Query("""
        SELECT w FROM WalkRecord w
        JOIN FETCH w.pet p
        LEFT JOIN FETCH p.profileImage
        JOIN FETCH p.breeds
        WHERE w.member.id = :memberId AND p.id = :petId
        """)
    List<WalkRecord> findAllByMemberIdAndPetId(@Param("memberId") Long memberId, @Param("petId") Long petId);

    // 특정 pet의 기간 내 산책 기록
    List<WalkRecord> findAllByPet_IdAndStartTimeBetween(Long petId, LocalDateTime start, LocalDateTime end);
}
