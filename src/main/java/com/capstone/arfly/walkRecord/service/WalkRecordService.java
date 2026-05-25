package com.capstone.arfly.walkRecord.service;

import com.capstone.arfly.common.exception.BusinessException;
import com.capstone.arfly.common.exception.ErrorCode;
import com.capstone.arfly.common.util.S3Uploader;
import com.capstone.arfly.pet.domain.Pet;
import com.capstone.arfly.pet.repository.PetRepository;
import com.capstone.arfly.walkRecord.domain.WalkRecord;
import com.capstone.arfly.walkRecord.dto.AssignedWalkDto;
import com.capstone.arfly.walkRecord.dto.AssignedWalkListResponse;
import com.capstone.arfly.walkRecord.dto.DayActivityDto;
import com.capstone.arfly.walkRecord.dto.DayDistanceDto;
import com.capstone.arfly.walkRecord.dto.PetReportDto;
import com.capstone.arfly.walkRecord.dto.UnassignedWalkDto;
import com.capstone.arfly.walkRecord.dto.UnassignedWalkListResponse;
import com.capstone.arfly.walkRecord.dto.WalkDateDto;
import com.capstone.arfly.walkRecord.dto.WalkPetDto;
import com.capstone.arfly.walkRecord.dto.WeeklyDistanceResponse;
import com.capstone.arfly.walkRecord.dto.WeeklyStepsResponse;
import com.capstone.arfly.walkRecord.repository.WalkRecordRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WalkRecordService {

    private final WalkRecordRepository walkRecordRepository;
    private final PetRepository petRepository;
    private final S3Uploader s3Uploader;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final List<String> DAY_ORDER = List.of("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN");
    private static final Map<DayOfWeek, String> DAY_MAP = Map.of(
            DayOfWeek.MONDAY, "MON",
            DayOfWeek.TUESDAY, "TUE",
            DayOfWeek.WEDNESDAY, "WED",
            DayOfWeek.THURSDAY, "THU",
            DayOfWeek.FRIDAY, "FRI",
            DayOfWeek.SATURDAY, "SAT",
            DayOfWeek.SUNDAY, "SUN"
    );

    // 미배정 산책 기록 목록 조회
    @Transactional(readOnly = true)
    public UnassignedWalkListResponse getUnassignedWalks(Long memberId) {
        List<WalkRecord> records = walkRecordRepository.findAllByMember_IdAndPetIsNull(memberId);
        List<UnassignedWalkDto> walks = records.stream()
                .map(this::toUnassignedWalkDto)
                .toList();
        return UnassignedWalkListResponse.builder()
                .totalCount(walks.size())
                .walks(walks)
                .build();
    }

    //  반려동물 배정
    @Transactional
    public void assignPet(Long memberId, Long walkId, Long petId) {
        WalkRecord walk = getWalkOrThrow(walkId, memberId);
        Pet pet = getPetOrThrow(petId, memberId);
        walk.assignPet(pet);
    }

    // 3. 반려동물 변경 (이미 배정된 기록)
    @Transactional
    public void reassignPet(Long memberId, Long walkId, Long petId) {
        WalkRecord walk = getWalkOrThrow(walkId, memberId);
        Pet pet = getPetOrThrow(petId, memberId);
        walk.assignPet(pet);
    }

    //  배정된 산책 기록 목록 조회
    @Transactional(readOnly = true)
    public AssignedWalkListResponse getAssignedWalks(Long memberId, Long petId) {
        List<WalkRecord> records = (petId == null)
                ? walkRecordRepository.findAllAssignedByMemberId(memberId)
                : walkRecordRepository.findAllByMemberIdAndPetId(memberId, petId);

        List<AssignedWalkDto> walks = records.stream()
                .map(this::toAssignedWalkDto)
                .toList();
        return AssignedWalkListResponse.builder()
                .walks(walks)
                .build();
    }

    // 주간 걸음 수 리포트
    @Transactional(readOnly = true)
    public WeeklyStepsResponse getWeeklyStepsReport(Long memberId, Long petId, LocalDate date) {
        Pet pet = getPetOrThrow(petId, memberId);
        List<WalkRecord> records = getWeeklyRecords(petId, date);
        Map<String, List<WalkRecord>> byDay = groupByDay(records);

        Map<String, DayActivityDto> week = new LinkedHashMap<>();
        double totalScore = 0.0;
        for (String day : DAY_ORDER) {
            List<WalkRecord> dayRecords = byDay.getOrDefault(day, Collections.emptyList());
            double score = dayRecords.isEmpty() ? 0.0
                    : dayRecords.stream().mapToDouble(WalkRecord::getActivityScore).sum();
            score = round1(score);
            week.put(day, new DayActivityDto(score));
            totalScore += score;
        }

        return WeeklyStepsResponse.builder()
                .pet(toPetReportDto(pet))
                .week(week)
                .averageActivityScore(round1(totalScore / 7))
                .build();
    }

    // 6. 주간 산책 거리 리포트
    @Transactional(readOnly = true)
    public WeeklyDistanceResponse getWeeklyDistanceReport(Long memberId, Long petId, LocalDate date) {
        Pet pet = getPetOrThrow(petId, memberId);
        List<WalkRecord> records = getWeeklyRecords(petId, date);
        Map<String, List<WalkRecord>> byDay = groupByDay(records);

        Map<String, DayDistanceDto> week = new LinkedHashMap<>();
        double totalDist = 0.0;
        for (String day : DAY_ORDER) {
            List<WalkRecord> dayRecords = byDay.getOrDefault(day, Collections.emptyList());
            double dist = round2(dayRecords.stream().mapToDouble(WalkRecord::getTotalDistance).sum());
            week.put(day, new DayDistanceDto(dist));
            totalDist += dist;
        }

        return WeeklyDistanceResponse.builder()
                .pet(toPetReportDto(pet))
                .week(week)
                .averageDistanceKm(round2(totalDist / 7))
                .build();
    }


    private WalkRecord getWalkOrThrow(Long walkId, Long memberId) {
        WalkRecord walk = walkRecordRepository.findById(walkId)
                .orElseThrow(() -> new BusinessException(ErrorCode.WALK_NOT_FOUND));
        if (!walk.getMember().getId().equals(memberId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }
        return walk;
    }

    private Pet getPetOrThrow(Long petId, Long memberId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PET_NOT_FOUND));
        if (!pet.getMember().getId().equals(memberId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }
        return pet;
    }

    private List<WalkRecord> getWeeklyRecords(Long petId, LocalDate date) {
        LocalDate monday = date.with(DayOfWeek.MONDAY);
        LocalDateTime start = monday.atStartOfDay();
        LocalDateTime end = monday.plusDays(6).atTime(LocalTime.MAX);
        return walkRecordRepository.findAllByPet_IdAndStartTimeBetween(petId, start, end);
    }

    private Map<String, List<WalkRecord>> groupByDay(List<WalkRecord> records) {
        Map<String, List<WalkRecord>> result = new HashMap<>();
        for (WalkRecord r : records) {
            String day = DAY_MAP.get(r.getStartTime().getDayOfWeek());
            result.computeIfAbsent(day, k -> new ArrayList<>()).add(r);
        }
        return result;
    }

    private UnassignedWalkDto toUnassignedWalkDto(WalkRecord r) {
        return UnassignedWalkDto.builder()
                .id(r.getId())
                .date(toWalkDateDto(r))
                .startTime(r.getStartTime().format(TIME_FORMATTER))
                .endTime(r.getEndTime().format(TIME_FORMATTER))
                .durationMinutes(ChronoUnit.MINUTES.between(r.getStartTime(), r.getEndTime()))
                .distanceKm(r.getTotalDistance())
                .score(r.getActivityScore())
                .build();
    }

    private AssignedWalkDto toAssignedWalkDto(WalkRecord r) {
        return AssignedWalkDto.builder()
                .id(r.getId())
                .date(toWalkDateDto(r))
                .startTime(r.getStartTime().format(TIME_FORMATTER))
                .endTime(r.getEndTime().format(TIME_FORMATTER))
                .durationMinutes(ChronoUnit.MINUTES.between(r.getStartTime(), r.getEndTime()))
                .distanceKm(r.getTotalDistance())
                .score(r.getActivityScore())
                .pet(toWalkPetDto(r.getPet()))
                .build();
    }

    private WalkDateDto toWalkDateDto(WalkRecord r) {
        LocalDateTime st = r.getStartTime();
        return WalkDateDto.builder()
                .year(st.getYear())
                .month(st.getMonthValue())
                .day(st.getDayOfMonth())
                .dayOfWeek(DAY_MAP.get(st.getDayOfWeek()))
                .build();
    }

    private WalkPetDto toWalkPetDto(Pet pet) {
        String image = null;
        if (pet.getProfileImage() != null && !pet.getProfileImage().getDeleted()) {
            image = s3Uploader.getPublicUrl(pet.getProfileImage().getFileKey());
        }
        return WalkPetDto.builder()
                .id(pet.getId())
                .name(pet.getName())
                .profileImage(image)
                .build();
    }

    private PetReportDto toPetReportDto(Pet pet) {
        int currentYear = LocalDate.now().getYear();
        String profileImage = null;
        if (pet.getProfileImage() != null && !pet.getProfileImage().getDeleted()) {
            profileImage = s3Uploader.getPublicUrl(pet.getProfileImage().getFileKey());
        }
        return PetReportDto.builder()
                .aiName(pet.getName())
                .breed(pet.getBreeds().getName())
                .age(currentYear - pet.getBirth())
                .profileImage(profileImage)
                .build();
    }

    private double round1(double value) {
        return Math.round(value * 10.0) / 10.0;
    }

    private double round2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
