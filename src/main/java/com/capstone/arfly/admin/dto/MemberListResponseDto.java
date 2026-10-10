package com.capstone.arfly.admin.dto;

import com.capstone.arfly.member.domain.Member;
import com.capstone.arfly.member.domain.MemberStatus;
import com.capstone.arfly.member.domain.Role;
import com.capstone.arfly.member.domain.SocialType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "[관리자] 회원 목록 항목")
public class MemberListResponseDto {

    @Schema(description = "회원 ID (상세 조회 시 사용)", example = "5")
    private Long memberId;

    @Schema(description = "닉네임", example = "유저123")
    private String nickName;

    @Schema(description = "아이디 (소셜 가입 회원은 이메일)", example = "test1234")
    private String userId;

    @Schema(description = "가입 방식 (LOCAL: 일반 가입, GOOGLE, KAKAO, NAVER)", example = "LOCAL")
    private String socialType;

    @Schema(description = "권한 (USER, DOCTOR, ADMIN)", example = "USER")
    private Role role;

    @Schema(description = "회원 상태 (ACTIVE: 정상, SUSPENDED: 정지, WITHDRAWN: 탈퇴)", example = "ACTIVE")
    private MemberStatus status;

    public static MemberListResponseDto from(Member member) {
        return MemberListResponseDto.builder()
                .memberId(member.getId())
                .nickName(member.getNickName())
                .userId(member.getUserId())
                .socialType(toSocialTypeName(member.getSocialType()))
                .role(member.getRole())
                .status(member.getStatus())
                .build();
    }

    // 일반 가입 회원은 socialType이 null이므로 LOCAL로 표시
    private static String toSocialTypeName(SocialType socialType) {
        return socialType == null ? "LOCAL" : socialType.name();
    }
}
