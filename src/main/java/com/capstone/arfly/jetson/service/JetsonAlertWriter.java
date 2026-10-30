package com.capstone.arfly.jetson.service;

import com.capstone.arfly.common.domain.File;
import com.capstone.arfly.common.dto.FileDetailDto;
import com.capstone.arfly.common.exception.BusinessException;
import com.capstone.arfly.common.exception.ErrorCode;
import com.capstone.arfly.common.repository.FileRepository;
import com.capstone.arfly.jetson.domain.JetsonAlert;
import com.capstone.arfly.jetson.domain.JetsonDevice;
import com.capstone.arfly.jetson.dto.JetsonAlertCreateRequestDto;
import com.capstone.arfly.jetson.repository.JetsonAlertRepository;
import com.capstone.arfly.jetson.repository.JetsonDeviceRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

// S3 업로드 후 DB 저장만 트랜잭션으로 묶기 위해 서비스와 분리
@Component
@RequiredArgsConstructor
public class JetsonAlertWriter {
    private final JetsonDeviceRepository jetsonDeviceRepository;
    private final JetsonAlertRepository jetsonAlertRepository;
    private final FileRepository fileRepository;

    // 사진 메타데이터와 이상 징후 알림 저장, 기기 마지막 접속 시각 갱신
    @Transactional
    public void saveAlert(long deviceId, JetsonAlertCreateRequestDto request, FileDetailDto fileDetail) {
        JetsonDevice device = jetsonDeviceRepository
                .findById(deviceId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_JETSON_TOKEN));

        File image = fileRepository.save(File.builder()
                .fileName(fileDetail.getOriginalFileName())
                .fileKey(fileDetail.getKey())
                .fileSize(fileDetail.getFileSize())
                .fileType(fileDetail.getFileType())
                .build());

        jetsonAlertRepository.save(JetsonAlert.builder()
                .device(device)
                .image(image)
                .diseaseName(request.getDiseaseName())
                .probability(request.getProbability())
                .build());

        device.updateLastSeenAt(LocalDateTime.now());
    }
}
