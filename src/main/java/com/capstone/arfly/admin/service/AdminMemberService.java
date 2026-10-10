package com.capstone.arfly.admin.service;

import com.capstone.arfly.admin.dto.MemberListResponseDto;
import com.capstone.arfly.member.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminMemberService {
    private final MemberRepository memberRepository;

    // 전체 회원 목록 조회 (최근 가입 순)
    @Transactional(readOnly = true)
    public List<MemberListResponseDto> getMembers() {
        return memberRepository.findAll(Sort.by(Sort.Direction.DESC, "id")).stream()
                .map(MemberListResponseDto::from)
                .toList();
    }
}
