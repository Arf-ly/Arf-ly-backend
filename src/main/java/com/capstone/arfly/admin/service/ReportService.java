package com.capstone.arfly.admin.service;

import com.capstone.arfly.admin.domain.Report;
import com.capstone.arfly.admin.domain.ReportReason;
import com.capstone.arfly.admin.domain.ReportStatus;
import com.capstone.arfly.admin.dto.ReportCreateRequestDto;
import com.capstone.arfly.admin.dto.ReportDetailResponseDto;
import com.capstone.arfly.admin.dto.ReportListResponseDto;
import com.capstone.arfly.admin.repository.ReportRepository;
import com.capstone.arfly.common.exception.BusinessException;
import com.capstone.arfly.common.exception.ErrorCode;
import com.capstone.arfly.common.exception.PostNotFoundException;
import com.capstone.arfly.community.domain.Post;
import com.capstone.arfly.community.repository.PostRepository;
import com.capstone.arfly.member.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    // 게시글 신고
    @Transactional
    public void reportPost(Long postId, long reporterId, ReportCreateRequestDto requestDto) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        if (post.getMember().getId() == reporterId) {
            throw new BusinessException(ErrorCode.CANNOT_REPORT_OWN_POST);
        }
        if (requestDto.getReason() == ReportReason.ETC && !StringUtils.hasText(requestDto.getDetail())) {
            throw new BusinessException(ErrorCode.REPORT_DETAIL_REQUIRED);
        }
        if (reportRepository.existsByReporterIdAndPostId(reporterId, postId)) {
            throw new BusinessException(ErrorCode.ALREADY_REPORTED_POST);
        }

        Report report = Report.builder()
                .reporter(memberRepository.getReferenceById(reporterId))
                .post(post)
                .reason(requestDto.getReason())
                .detail(requestDto.getDetail())
                .build();
        try {
            // 동시에 같은 신고가 들어온 경우 유니크 제약으로 걸러냄
            reportRepository.saveAndFlush(report);
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException(ErrorCode.ALREADY_REPORTED_POST);
        }
    }

    // 처리 대기 중인 신고 목록 조회
    @Transactional(readOnly = true)
    public List<ReportListResponseDto> getPendingReports() {
        return reportRepository.findAllByStatusOrderByCreatedAtAsc(ReportStatus.PENDING).stream()
                .map(ReportListResponseDto::from)
                .toList();
    }

    // 신고 상세 조회
    @Transactional(readOnly = true)
    public ReportDetailResponseDto getReportDetail(Long reportId) {
        Report report = reportRepository
                .findWithReporterAndPostById(reportId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_REPORT_NOT_FOUND));
        return ReportDetailResponseDto.from(report);
    }

    // 신고 반려 (처리 대기 중인 신고만 가능)
    @Transactional
    public void rejectReport(Long reportId) {
        Report report = reportRepository
                .findById(reportId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_REPORT_NOT_FOUND));
        if (report.getStatus() != ReportStatus.PENDING) {
            throw new BusinessException(ErrorCode.POST_REPORT_ALREADY_PROCESSED);
        }
        report.reject();
    }
}
