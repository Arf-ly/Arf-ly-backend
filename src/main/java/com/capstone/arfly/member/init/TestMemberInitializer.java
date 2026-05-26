package com.capstone.arfly.member.init;

import com.capstone.arfly.iot.domain.IoTDevice;
import com.capstone.arfly.iot.repository.IoTDeviceRepository;
import com.capstone.arfly.member.domain.Member;
import com.capstone.arfly.member.repository.MemberRepository;
import com.capstone.arfly.pet.domain.Breeds;
import com.capstone.arfly.pet.domain.Pet;
import com.capstone.arfly.pet.domain.Sex;
import com.capstone.arfly.pet.domain.Species;
import com.capstone.arfly.pet.repository.BreedsRepository;
import com.capstone.arfly.pet.repository.PetRepository;
import com.capstone.arfly.walkRecord.domain.WalkRecord;
import com.capstone.arfly.walkRecord.repository.WalkRecordRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@Order(2)
@RequiredArgsConstructor
public class TestMemberInitializer implements ApplicationRunner {

    @Value("${app.user.id}")
    private String userId;

    @Value("${app.user.password}")
    private String userPassword;

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final PetRepository petRepository;
    private final BreedsRepository breedsRepository;
    private final IoTDeviceRepository iotDeviceRepository;
    private final WalkRecordRepository walkRecordRepository;

    private static final String DEVICE_UID = "TEST-DEVICE-001";

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {

        // ── 1. 테스트 멤버 생성 ──────────────────────────────────────
        Member member = memberRepository.findByUserId(userId).orElseGet(() -> {
            log.info("[TestInit] 테스트 멤버 생성: {}", userId);
            return memberRepository.save(
                    Member.builder()
                            .userId(userId)
                            .password(passwordEncoder.encode(userPassword))
                            .nickName("testUser")
                            .build()
            );
        });

        // 이미 산책 데이터가 있으면 전부 skip
        if (!walkRecordRepository.findAllByMember_IdAndPetIsNull(member.getId()).isEmpty()
                || !walkRecordRepository.findAllAssignedByMemberId(member.getId()).isEmpty()) {
            log.info("[TestInit] 산책 더미 데이터가 이미 존재합니다. skip");
            return;
        }

        // ── 2. IoT 기기 생성 ─────────────────────────────────────────
        IoTDevice device = iotDeviceRepository.findByDeviceUid(DEVICE_UID).orElseGet(() -> {
            log.info("[TestInit] IoT 기기 생성: {}", DEVICE_UID);
            return iotDeviceRepository.save(
                    IoTDevice.builder()
                            .member(member)
                            .deviceUid(DEVICE_UID)
                            .build()
            );
        });

        // ── 3. 품종 조회 (BreedDataInit @Order(1) 이후 실행 보장) ────
        Breeds akita = breedsRepository.findByName("아키타")
                .orElseThrow(() -> new IllegalStateException("품종 '아키타'를 찾을 수 없습니다."));

        // ── 4. 반려동물 3마리 생성 ────────────────────────────────────
        List<Pet> existingPets = petRepository.findAllByMemberId(member.getId());

        Pet noRecord = getOrCreatePet(existingPets, member, akita, "바둑이", Sex.MALE, 2022);
        Pet pet50_1 = getOrCreatePet(existingPets, member, akita, "초코", Sex.FEMALE, 2021);
        Pet pet50_2 = getOrCreatePet(existingPets, member, akita, "두부", Sex.MALE, 2020);

        // ── 5. 미배정 산책 기록 10개 (pet = null) ────────────────────
        // 오늘부터 1~10일 전, 산책 시간/거리/점수 다양하게
        double[] distances = {3.2, 2.5, 1.8, 4.0, 2.1, 3.7, 1.5, 2.9, 3.5, 4.2};
        double[] scores = {87, 72, 65, 91, 70, 83, 60, 78, 85, 90};
        int[] durations = {45, 45, 30, 50, 35, 40, 25, 40, 45, 55};

        for (int i = 0; i < 10; i++) {
            LocalDateTime start = LocalDateTime.now().minusDays(i + 1)
                    .withHour(8).withMinute(0).withSecond(0).withNano(0);
            walkRecordRepository.save(WalkRecord.builder()
                    .member(member)
                    .device(device)
                    .pet(null)
                    .startTime(start)
                    .endTime(start.plusMinutes(durations[i]))
                    .activityScore(scores[i])
                    .totalDistance(distances[i])
                    .build());
        }
        log.info("[TestInit] 미배정 산책 기록 10개 생성 완료");

        // ── 6. 초코 산책 기록 50개 ────────────────────────────────────
        // 오늘부터 50일 전까지 하루 1개, 아침/저녁 교차
        for (int i = 0; i < 50; i++) {
            LocalDateTime start = LocalDateTime.now().minusDays(i + 1)
                    .withHour(i % 2 == 0 ? 8 : 18)
                    .withMinute(i % 3 == 0 ? 0 : 30)
                    .withSecond(0).withNano(0);
            double dist = 1.5 + (i % 7) * 0.5;          // 1.5 ~ 4.5 km 패턴
            double score = 55 + (i % 10) * 4.0;           // 55 ~ 91 패턴
            int dur = 25 + (i % 6) * 5;              // 25 ~ 50분 패턴

            WalkRecord walk = WalkRecord.builder()
                    .member(member)
                    .device(device)
                    .startTime(start)
                    .endTime(start.plusMinutes(dur))
                    .activityScore(score)
                    .totalDistance(dist)
                    .build();
            walk.assignPet(pet50_1);
            walkRecordRepository.save(walk);
        }
        log.info("[TestInit] 초코 산책 기록 50개 생성 완료");

        // ── 7. 두부 산책 기록 50개 ────────────────────────────────────
        // 초코와 다른 시간대/패턴
        for (int i = 0; i < 50; i++) {
            LocalDateTime start = LocalDateTime.now().minusDays(i + 1)
                    .withHour(i % 2 == 0 ? 7 : 17)
                    .withMinute(i % 4 == 0 ? 0 : 15)
                    .withSecond(0).withNano(0);
            double dist = 2.0 + (i % 5) * 0.7;          // 2.0 ~ 4.8 km 패턴
            double score = 60 + (i % 8) * 4.5;            // 60 ~ 91.5 패턴
            int dur = 30 + (i % 5) * 6;              // 30 ~ 54분 패턴

            WalkRecord walk = WalkRecord.builder()
                    .member(member)
                    .device(device)
                    .startTime(start)
                    .endTime(start.plusMinutes(dur))
                    .activityScore(score)
                    .totalDistance(dist)
                    .build();
            walk.assignPet(pet50_2);
            walkRecordRepository.save(walk);
        }
        log.info("[TestInit] 두부 산책 기록 50개 생성 완료");

        log.info("[TestInit] 더미 데이터 생성 완료 ✅");
        log.info("[TestInit] 바둑이({}): 산책 기록 0개", noRecord.getId());
        log.info("[TestInit] 초코({}): 산책 기록 50개", pet50_1.getId());
        log.info("[TestInit] 두부({}): 산책 기록 50개", pet50_2.getId());
    }

    private Pet getOrCreatePet(List<Pet> existing, Member member, Breeds breeds,
                               String name, Sex sex, int birthYear) {
        return existing.stream()
                .filter(p -> p.getName().equals(name))
                .findFirst()
                .orElseGet(() -> {
                    log.info("[TestInit] 반려동물 생성: {}", name);
                    return petRepository.save(
                            Pet.builder()
                                    .member(member)
                                    .breeds(breeds)
                                    .name(name)
                                    .species(Species.DOG)
                                    .sex(sex)
                                    .birth(birthYear)
                                    .neutered(true)
                                    .weight(5.0)
                                    .build()
                    );
                });
    }
}
