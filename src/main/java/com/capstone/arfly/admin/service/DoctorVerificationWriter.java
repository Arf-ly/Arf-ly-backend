package com.capstone.arfly.admin.service;

import com.capstone.arfly.admin.domain.DoctorVerification;
import com.capstone.arfly.admin.repository.DoctorVerificationRepository;
import com.capstone.arfly.common.domain.File;
import com.capstone.arfly.common.dto.FileDetailDto;
import com.capstone.arfly.common.repository.FileRepository;
import com.capstone.arfly.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DoctorVerificationWriter {
    private final DoctorVerificationRepository doctorVerificationRepository;
    private final FileRepository fileRepository;
    private final MemberRepository memberRepository;

    // 면허증 파일과 인증 신청을 하나의 트랜잭션으로 저장
    @Transactional
    public void saveVerification(long memberId, FileDetailDto fileDetail) {
        File licenseImage = File.builder()
                .fileName(fileDetail.getOriginalFileName())
                .fileKey(fileDetail.getKey())
                .fileSize(fileDetail.getFileSize())
                .fileType(fileDetail.getFileType())
                .build();
        fileRepository.save(licenseImage);

        DoctorVerification verification = DoctorVerification.builder()
                .member(memberRepository.getReferenceById(memberId))
                .licenseImage(licenseImage)
                .build();
        doctorVerificationRepository.save(verification);
    }
}
