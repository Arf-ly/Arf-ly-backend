package com.capstone.arfly.hospital.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "지도 리스트 조회 응답 정보")
public class HospitalListUserResponse {

    @Schema(description = "사용자 위도")
    private Double latitude;

    @Schema(description = "사용자 경도")
    private Double longitude;

    @Schema(description = "사용자 도로명 주소")
    private String roadAddress;

    @Schema(description = "병원 리스트")
    private List<HospitalListResponse> hospitals;
}