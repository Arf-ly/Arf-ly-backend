package com.capstone.arfly.community.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "파일 정보")
public class FileDto {
    @Schema(description = "파일 id")
    long fileId;

    @Schema(description = "파일 이미지 URL")
    String fileUrl;
}
