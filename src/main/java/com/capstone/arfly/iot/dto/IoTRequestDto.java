package com.capstone.arfly.iot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

public class IoTRequestDto {

    @Getter
    @NoArgsConstructor
    @Schema(description = "기기 최초 등록 요청")
    public static class Register {
        private Long memberId;
        private String deviceUid;
    }

    @Getter
    @NoArgsConstructor
    @Schema(description = "라즈베리파이 피코 원본 산책 데이터 업로드 요청")
    public static class UploadWalk {

        @Schema(description = "기기 식별용 고유 UID", example = "550e8400-...")
        private String deviceUid;

        @JsonProperty("start_time")
        @Schema(description = "산책 시작 시간", example = "17:00:00")
        private String startTime;

        @JsonProperty("end_time")
        @Schema(description = "산책 종료 시간", example = "18:00:00")
        private String endTime;

        @Schema(description = "산책 날짜", example = "December 0th, 2026")
        private String date;

        @Schema(description = "초 단위 GPS 및 가속도 센서 기록 배열")
        private List<SensorRecord> records;
    }

    @Getter
    @NoArgsConstructor
    public static class SensorRecord {
        private double lon; // 경도
        private double lat; // 위도
        private double ax;  // 가속도 X
        private double ay;  // 가속도 Y
        private double az;  // 가속도 Z
    }
}