package com.capstone.arfly.iot.service;

import com.capstone.arfly.common.exception.BusinessException;
import com.capstone.arfly.common.exception.ErrorCode;
import com.capstone.arfly.iot.domain.IoTDevice;
import com.capstone.arfly.iot.dto.IoTRequestDto;
import com.capstone.arfly.iot.repository.IoTDeviceRepository;
import com.capstone.arfly.member.domain.Member;
import com.capstone.arfly.walkRecord.domain.WalkRecord;
import com.capstone.arfly.walkRecord.repository.WalkRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional
public class IoTService {

    private final IoTDeviceRepository iotDeviceRepository;
    private final WalkRecordRepository walkRecordRepository; // walkRecord 창고 사용

    private static final int EARTH_RADIUS = 6371; // 지구 반지름 (km)

    private final com.capstone.arfly.member.repository.MemberRepository memberRepository; // MemberRepository 주입 필요

    // IoTService.java
    public void registerDevice(Long memberId, String deviceUid) {
        // 1. 회원 확인 (로그인 유저 정보 활용)
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_EXISTS));

        // 2. 피코가 이미 가지고 있던 그 UID를 DB에 그대로 저장! (생성하는 게 아니라 매핑하는 것)
        IoTDevice device = IoTDevice.builder()
                .member(member)
                .deviceUid(deviceUid) // 피코가 들고온 그 값을 그대로 저장
                .build();
        iotDeviceRepository.save(device);
    }

    public void uploadWalkData(IoTRequestDto.UploadWalk request) {
        // 피코가 보낸 deviceUid로 기기 조회
        IoTDevice device = iotDeviceRepository.findByDeviceUid(request.getDeviceUid())
                .orElseThrow(() -> new BusinessException(ErrorCode.DEVICE_NOT_FOUND));

        double totalDistance = Math.round(calculateTotalDistance(request.getRecords()) * 10.0) / 10.0;
        double activityScore = Math.round(calculateActivityScore(request.getRecords()) * 10.0) / 10.0;

        LocalDateTime startDateTime = convertToLocalDateTime(request.getDate(), request.getStartTime());
        LocalDateTime endDateTime = convertToLocalDateTime(request.getDate(), request.getEndTime());

        WalkRecord walkRecord = WalkRecord.builder()
                .startTime(startDateTime)
                .endTime(endDateTime)
                .totalDistance(totalDistance)
                .activityScore(activityScore)
                .device(device)
                .member(device.getMember())
                .build();

        walkRecordRepository.save(walkRecord);
    }

    // --- 하버사인 기반 총 이동 거리 계산기 ---
    private double calculateTotalDistance(List<IoTRequestDto.SensorRecord> records) {
        double totalDist = 0.0;
        if (records == null || records.size() < 2) return 0.0;

        // 유효하지 않은 GPS 좌표(0.0) 먼저 제거
        List<IoTRequestDto.SensorRecord> validRecords = records.stream()
                .filter(r -> r.getLat() != 0.0 && r.getLon() != 0.0)
                .toList();

        if (validRecords.size() < 2) return 0.0;

        for (int i = 0; i < validRecords.size() - 1; i++) {
            IoTRequestDto.SensorRecord current = validRecords.get(i);
            IoTRequestDto.SensorRecord next = validRecords.get(i + 1);

            totalDist += haversine(current.getLat(), current.getLon(), next.getLat(), next.getLon());
        }
        return totalDist;
    }

    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians((lat2 - lat1));
        double dLong = Math.toRadians((lon2 - lon1));

        double a = Math.pow(Math.sin(dLat / 2), 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.pow(Math.sin(dLong / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }

    // --- 가속도 센서 기반 만보기 활동량 점수 알고리즘 (1보 = 1점, 무제한) ---
    private double calculateActivityScore(List<IoTRequestDto.SensorRecord> records) {
        if (records == null || records.isEmpty()) return 0.0;

        int stepCount = 0;
        // 임계값(Threshold): 중력(9.8) + 발을 디딜 때의 충격. 강아지 크기에 따라 11.0 ~ 13.0 사이로 튜닝하세요!
        double stepThreshold = 12.0;
        boolean isStepping = false;

        for (IoTRequestDto.SensorRecord r : records) {
            // 3축 가속도 벡터의 크기 계산
            double magnitude = Math.sqrt(r.getAx() * r.getAx() + r.getAy() * r.getAy() + r.getAz() * r.getAz());

            // 가속도가 임계값을 넘고, 이전에 걸음 상태가 아니었다면 1보 추가!
            if (magnitude > stepThreshold && !isStepping) {
                stepCount++;
                isStepping = true;
            }
            // 가속도가 다시 평온한 상태(임계값 아래)로 돌아오면 다음 걸음을 인식할 준비
            else if (magnitude < stepThreshold) {
                isStepping = false;
            }
        }

        // 비즈니스 룰 반영: 1걸음 = 1점, 최대치 제한 없음!
        // (DB 엔티티의 activityScore 타입이 Double이므로 형변환만 해줍니다)
        return (double) stepCount;
    }


    // --- 피코 특유의 문자열 날짜 포맷 변환기 ---
    private LocalDateTime convertToLocalDateTime(String dateStr, String timeStr) {
        try {
            // "December 0th, 2026" 같은 포맷에서 'th', 'st', 'nd', 'rd' 같은 서수를 제거하는 전처리 필요
            String cleanDate = dateStr.replaceAll("(?<=\\d)(st|nd|rd|th),", ",");

            // "December 0, 2026" 형식에 맞게 파싱 포맷 지정
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH);
            LocalDate date = LocalDate.parse(cleanDate, dateFormatter);
            LocalTime time = LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("HH:mm:ss"));

            return LocalDateTime.of(date, time);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.INVALID_SENSOR_DATA);
        }
    }
}