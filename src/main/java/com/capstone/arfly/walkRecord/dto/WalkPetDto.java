package com.capstone.arfly.walkRecord.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "산책 기록에 배정된 반려동물 요약 정보")
public class WalkPetDto {

    @Schema(description = "반려동물 ID", example = "61")
    private Long id;

    @Schema(description = "반려동물 이름", example = "초코")
    private String name;

    @Schema(description = "프로필 사진 URL")
    private String profileImage;
}
