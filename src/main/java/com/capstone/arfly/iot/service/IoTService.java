package com.capstone.arfly.iot.service;

import com.capstone.arfly.common.exception.BusinessException;
import com.capstone.arfly.common.exception.ErrorCode;
import com.capstone.arfly.iot.domain.IoTDevice;
import com.capstone.arfly.iot.dto.IoTRequestDto;
import com.capstone.arfly.iot.repository.IoTDeviceRepository;
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

    public String registerDevice(IoTRequestDto.Register request) {
        // 1. 회원 확인
        com.capstone.arfly.member.domain.Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_EXISTS));

        // 2. UID 생성 및 저장
        String deviceUid = java.util.UUID.randomUUID().toString();
        IoTDevice device = IoTDevice.builder()
                .member(member)
                .deviceUid(deviceUid)
                .build();
        iotDeviceRepository.save(device);

        return deviceUid;
    }
    public void uploadWalkData(String deviceUid, IoTRequestDto.UploadWalk request) {

        IoTDevice device = iotDeviceRepository.findByDeviceUid(deviceUid)
                .orElseThrow(() -> new BusinessException(ErrorCode.DEVICE_NOT_FOUND));

        // 2. 피코가 보낸 records 배열을 순회하며 총 이동 거리(km) 직접 계산 (하버사인)
        double totalDistance = calculateTotalDistance(request.getRecords());

        // 3. 가속도(ax, ay, az) 배열을 분석하여 활동량 점수 계산
        double activityScore = calculateActivityScore(request.getRecords());

        // 4. 피코의 시간 문자열 포맷("December 0th, 2026", "17:00:00")을 LocalDateTime으로 변환
        LocalDateTime startDateTime = convertToLocalDateTime(request.getDate(), request.getStartTime());
        LocalDateTime endDateTime = convertToLocalDateTime(request.getDate(), request.getEndTime());

        // 5. 핵심: walkRecord 도메인의 엔티티를 빌드하되, 계산된 값들을 쏙 집어넣음
        // status는 기본값인 false(미배정)로 생성되며 pet은 null 상태
        WalkRecord walkRecord = WalkRecord.builder()
                .startTime(startDateTime)
                .endTime(endDateTime)
                .totalDistance(totalDistance)   // ◀ 서버가 직접 구한 거리 값
                .activityScore(activityScore)   // ◀ 서버가 직접 구한 점수 값
                .device(device)
                .member(device.getMember())
                .build();

        // 6. walkRecord 창고에 쾅 저장!
        walkRecordRepository.save(walkRecord);
    }

    // --- 하버사인 기반 총 이동 거리 계산기 ---
    private double calculateTotalDistance(List<IoTRequestDto.SensorRecord> records) {
        double totalDist = 0.0;
        if (records == null || records.size() < 2) return 0.0;

        for (int i = 0; i < records.size() - 1; i++) {
            IoTRequestDto.SensorRecord current = records.get(i);
            IoTRequestDto.SensorRecord next = records.get(i + 1);

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