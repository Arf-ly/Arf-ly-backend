package com.capstone.arfly.admin.service;

import com.capstone.arfly.admin.domain.Report;
import com.capstone.arfly.admin.domain.ReportReason;
import com.capstone.arfly.admin.dto.ReportCreateRequestDto;
import com.capstone.arfly.admin.repository.ReportRepository;
import com.capstone.arfly.common.exception.BusinessException;
import com.capstone.arfly.common.exception.ErrorCode;
import com.capstone.arfly.common.exception.PostNotFoundException;
import com.capstone.arfly.community.domain.Post;
import com.capstone.arfly.community.repository.PostRepository;
import com.capstone.arfly.member.repository.MemberRepository;
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
}
