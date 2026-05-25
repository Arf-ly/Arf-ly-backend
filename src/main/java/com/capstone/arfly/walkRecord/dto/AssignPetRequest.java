package com.capstone.arfly.walkRecord.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "반려동물 배정 / 변경 요청")
public class AssignPetRequest {

    @Schema(description = "배정할 반려동물 ID", example = "61", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long petId;
}
