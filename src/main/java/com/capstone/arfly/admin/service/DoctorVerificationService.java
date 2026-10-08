package com.capstone.arfly.admin.service;

import com.capstone.arfly.admin.domain.VerificationStatus;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
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
}
