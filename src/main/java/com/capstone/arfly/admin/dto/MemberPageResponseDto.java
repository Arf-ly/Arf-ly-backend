package com.capstone.arfly.admin.dto;

import com.capstone.arfly.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@Builder
@Schema(description = "[관리자] 회원 목록 페이지 응답")
public class MemberPageResponseDto {

    @Schema(description = "회원 목록")
    private List<MemberListResponseDto> members;

    @Schema(description = "현재 페이지 번호 (0부터 시작)", example = "0")
    private int page;

    @Schema(description = "페이지 크기", example = "20")
    private int size;

    @Schema(description = "전체 회원 수", example = "35")
    private long totalCount;

    @Schema(description = "전체 페이지 수", example = "2")
    private int totalPages;

    @Schema(description = "다음 페이지 존재 여부", example = "true")
    private boolean hasNext;

    public static MemberPageResponseDto from(Page<Member> memberPage) {
        return MemberPageResponseDto.builder()
                .members(memberPage.getContent().stream()
                        .map(MemberListResponseDto::from)
                        .toList())
                .page(memberPage.getNumber())
                .size(memberPage.getSize())
                .totalCount(memberPage.getTotalElements())
                .totalPages(memberPage.getTotalPages())
                .hasNext(memberPage.hasNext())
                .build();
    }
}
