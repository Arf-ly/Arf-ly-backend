package com.capstone.arfly.walkRecord.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "주간 리포트에 포함되는 반려동물 기본 정보")
public class PetReportDto {

    @Schema(description = "반려동물 이름", example = "초코")
    private String aiName;

    @Schema(description = "견종", example = "포메라니안")
    private String breed;

    @Schema(description = "나이 (현재 연도 - 출생 연도)", example = "3")
    private int age;

    @Schema(description = "프로필 사진 URL (없으면 null)", example = "https://arfly-s3-bucket-2026.s3.ap-northeast-2.amazonaws.com/pets/abc.jpg")
    private String profileImage;
}
