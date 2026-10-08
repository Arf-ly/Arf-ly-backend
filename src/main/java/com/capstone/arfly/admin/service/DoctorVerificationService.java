package com.capstone.arfly.admin.service;

import com.capstone.arfly.admin.domain.DoctorVerification;
import com.capstone.arfly.admin.domain.VerificationStatus;
import com.capstone.arfly.admin.dto.DoctorVerificationDetailResponseDto;
import com.capstone.arfly.admin.dto.DoctorVerificationListResponseDto;
import com.capstone.arfly.admin.repository.DoctorVerificationRepository;
import com.capstone.arfly.common.constant.S3DIRNAME;
import com.capstone.arfly.common.domain.FileType;
import com.capstone.arfly.common.dto.FileDetailDto;
import com.capstone.arfly.common.exception.BusinessException;
import com.capstone.arfly.common.exception.ErrorCode;
import com.capstone.arfly.common.util.S3Uploader;
import com.capstone.arfly.member.domain.Member;
import com.capstone.arfly.member.domain.Role;
import com.capstone.arfly.member.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class DoctorVerificationService {
    private final DoctorVerificationRepository doctorVerificationRepository;
    private final DoctorVerificationWriter doctorVerificationWriter;
    private final MemberRepository memberRepository;
    private final S3Uploader s3Uploader;

    // 의사 인증 신청 (면허증 사진 업로드)
    public void apply(long memberId, MultipartFile licenseImage) {
        Member member =
                memberRepository.findById(memberId).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_EXISTS));
        if (member.getRole() == Role.DOCTOR) {
            throw new BusinessException(ErrorCode.ALREADY_DOCTOR);
        }
        if (doctorVerificationRepository.existsByMemberIdAndStatus(memberId, VerificationStatus.PENDING)) {
            throw new BusinessException(ErrorCode.DOCTOR_VERIFICATION_ALREADY_PENDING);
        }

        FileDetailDto fileDetail = s3Uploader.makeMetaData(licenseImage, S3DIRNAME.DOCTOR_LICENSE.name());
        // 면허증은 이미지만 허용
        if (fileDetail.getFileType() != FileType.IMAGE) {
            throw new BusinessException(ErrorCode.INVALID_FILE_TYPE);
        }

        s3Uploader.uploadFile(fileDetail.getKey(), licenseImage);
        try {
            doctorVerificationWriter.saveVerification(memberId, fileDetail);
        } catch (DataAccessException e) {
            log.error("의사 인증 신청 저장 실패로 업로드된 면허증 파일을 정리합니다.", e);
            s3Uploader.deleteFile(fileDetail.getKey());
            throw new BusinessException(ErrorCode.DOCTOR_VERIFICATION_SAVE_FAILED);
        }
    }

    // 심사 대기 중인 인증 신청 목록 조회
    @Transactional(readOnly = true)
    public List<DoctorVerificationListResponseDto> getPendingVerifications() {
        return doctorVerificationRepository.findAllByStatusOrderByCreatedAtAsc(VerificationStatus.PENDING).stream()
                .map(DoctorVerificationListResponseDto::from)
                .toList();
    }

    // 인증 신청 상세 조회
    @Transactional(readOnly = true)
    public DoctorVerificationDetailResponseDto getVerificationDetail(Long verificationId) {
        DoctorVerification verification = doctorVerificationRepository
                .findWithMemberAndLicenseImageById(verificationId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DOCTOR_VERIFICATION_NOT_FOUND));
        String licenseImageUrl =
                s3Uploader.getUrlFile(verification.getLicenseImage().getFileKey());
        return DoctorVerificationDetailResponseDto.of(verification, licenseImageUrl);
    }

    // 인증 승인
    @Transactional
    public void approve(Long verificationId) {
        DoctorVerification verification = getPendingVerification(verificationId);
        verification.approve();
        verification.getMember().changeRole(Role.DOCTOR);
    }

    // 인증 반려
    @Transactional
    public void reject(Long verificationId, String reason) {
        DoctorVerification verification = getPendingVerification(verificationId);
        verification.reject(reason);
    }

    // 심사 대기 중인 신청만 승인/반려 가능
    private DoctorVerification getPendingVerification(Long verificationId) {
        DoctorVerification verification = doctorVerificationRepository
                .findById(verificationId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DOCTOR_VERIFICATION_NOT_FOUND));
        if (verification.getStatus() != VerificationStatus.PENDING) {
            throw new BusinessException(ErrorCode.DOCTOR_VERIFICATION_ALREADY_REVIEWED);
        }
        return verification;
    }
}
