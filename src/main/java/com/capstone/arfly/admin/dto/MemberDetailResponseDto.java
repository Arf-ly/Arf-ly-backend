package com.capstone.arfly.admin.dto;

import com.capstone.arfly.community.domain.Post;
import com.capstone.arfly.member.domain.Member;
import com.capstone.arfly.member.domain.MemberStatus;
import com.capstone.arfly.member.domain.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "[관리자] 회원 상세 응답")
public class MemberDetailResponseDto {

    @Schema(description = "회원 ID", example = "5")
    private Long memberId;

    @Schema(description = "닉네임", example = "유저123")
    private String nickName;

    @Schema(description = "로그인 아이디 (소셜 가입 회원은 이메일)", example = "test1234")
    private String loginId;

    @Schema(description = "가입 방식 (LOCAL: 일반 가입, GOOGLE, KAKAO, NAVER)", example = "LOCAL")
    private String socialType;

    @Schema(description = "권한 (USER, DOCTOR, ADMIN)", example = "USER")
    private Role role;

    @Schema(description = "회원 상태 (ACTIVE: 정상, SUSPENDED: 정지, WITHDRAWN: 탈퇴)", example = "ACTIVE")
    private MemberStatus status;

    @Schema(description = "전화번호 (소셜 가입 회원은 null일 수 있음)", example = "010-1111-2222")
    private String phoneNumber;

    @Schema(description = "도로명 주소 (미등록 시 null)", example = "경북 경산시 대학로 280")
    private String roadAddress;

    @Schema(description = "게시글 수", example = "3")
    private long postCount;

    @Schema(description = "댓글 수", example = "12")
    private long commentCount;

    @Schema(description = "반려동물 수", example = "1")
    private long petCount;

    @Schema(description = "작성한 게시글 목록 (최신순)")
    private List<PostSummary> posts;

    public static MemberDetailResponseDto of(Member member, long commentCount, long petCount, List<Post> posts) {
        return MemberDetailResponseDto.builder()
                .memberId(member.getId())
                .nickName(member.getNickName())
                .loginId(member.getUserId())
                .socialType(MemberListResponseDto.toSocialTypeName(member.getSocialType()))
                .role(member.getRole())
                .status(member.getStatus())
                .phoneNumber(member.getPhoneNumber())
                .roadAddress(member.getRoad_address())
                .postCount(posts.size())
                .commentCount(commentCount)
                .petCount(petCount)
                .posts(posts.stream().map(PostSummary::from).toList())
                .build();
    }

    @Getter
    @Builder
    public static class PostSummary {
        @Schema(description = "게시글 ID (게시글 상세 페이지 링크용)", example = "10")
        private Long postId;

        @Schema(description = "게시글 제목", example = "우리 집 강아지 첫 산책!")
        private String title;

        @Schema(description = "작성 일시", example = "2026-10-10T14:30:00")
        private LocalDateTime createdAt;

        public static PostSummary from(Post post) {
            return PostSummary.builder()
                    .postId(post.getId())
                    .title(post.getTitle())
                    .createdAt(post.getCreatedAt())
                    .build();
        }
    }
}
