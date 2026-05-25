package com.capstone.arfly.walkRecord.repository;

import com.capstone.arfly.walkRecord.domain.WalkRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface WalkRecordRepository extends JpaRepository<WalkRecord, Long> {

    // 1. 특정 회원의 '미배정(status = false)' 산책 기록 목록 조회
    // (홈 화면 및 미배정 알림에 사용)
    List<WalkRecord> findByMemberIdAndStatusFalse(Long memberId);

    // 2. 특정 펫에 '배정완료(status = true)'된 모든 산책 기록 조회
    // (반려동물별 전체 산책 이력)
    List<WalkRecord> findByPetIdAndStatusTrue(Long petId);

    // 3. 특정 펫의 특정 기간(주간/월간) 산책 기록 조회
    // (주간 활동량 통계 API에 사용)
    List<WalkRecord> findByPetIdAndStatusTrueAndStartTimeBetween(Long petId, LocalDateTime start, LocalDateTime end);
}