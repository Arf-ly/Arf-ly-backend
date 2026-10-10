package com.capstone.arfly.admin.service;

import com.capstone.arfly.admin.dto.MemberDetailResponseDto;
import com.capstone.arfly.admin.dto.MemberListResponseDto;
import com.capstone.arfly.common.exception.BusinessException;
import com.capstone.arfly.common.exception.ErrorCode;
import com.capstone.arfly.community.domain.Post;
import com.capstone.arfly.community.repository.CommentRepository;
import com.capstone.arfly.community.repository.PostRepository;
import com.capstone.arfly.member.domain.Member;
import com.capstone.arfly.member.domain.MemberStatus;
import com.capstone.arfly.member.domain.Role;
import com.capstone.arfly.member.repository.MemberRepository;
import com.capstone.arfly.pet.repository.PetRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminMemberService {
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PetRepository petRepository;

    // 전체 회원 목록 조회 (최근 가입 순)
    @Transactional(readOnly = true)
    public List<MemberListResponseDto> getMembers() {
        return memberRepository.findAll(Sort.by(Sort.Direction.DESC, "id")).stream()
                .map(MemberListResponseDto::from)
                .toList();
    }

    // 회원 상세 조회
    @Transactional(readOnly = true)
    public MemberDetailResponseDto getMemberDetail(Long memberId) {
        Member member =
                memberRepository.findById(memberId).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_EXISTS));
        List<Post> posts = postRepository.findAllByMemberIdOrderByCreatedAtDesc(memberId);
        long commentCount = commentRepository.countByMember(member);
        long petCount = petRepository.countByMemberId(memberId);
        return MemberDetailResponseDto.of(member, commentCount, petCount, posts);
    }

    // 회원 정지
    @Transactional
    public void suspendMember(Long memberId) {
        Member member = getMember(memberId);
        if (member.getRole() == Role.ADMIN) {
            throw new BusinessException(ErrorCode.CANNOT_SUSPEND_ADMIN);
        }
        if (member.getStatus() == MemberStatus.WITHDRAWN) {
            throw new BusinessException(ErrorCode.WITHDRAWN_MEMBER);
        }
        if (member.getStatus() == MemberStatus.SUSPENDED) {
            throw new BusinessException(ErrorCode.MEMBER_ALREADY_SUSPENDED);
        }
        member.changeStatus(MemberStatus.SUSPENDED);
    }

    // 회원 정지 해제
    @Transactional
    public void unsuspendMember(Long memberId) {
        Member member = getMember(memberId);
        if (member.getStatus() != MemberStatus.SUSPENDED) {
            throw new BusinessException(ErrorCode.MEMBER_NOT_SUSPENDED);
        }
        member.changeStatus(MemberStatus.ACTIVE);
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_EXISTS));
    }
}
