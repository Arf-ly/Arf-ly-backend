package com.capstone.arfly.jetson.service;

import com.capstone.arfly.common.constant.S3DIRNAME;
import com.capstone.arfly.common.domain.FileType;
import com.capstone.arfly.common.dto.FileDetailDto;
import com.capstone.arfly.common.exception.BusinessException;
import com.capstone.arfly.common.exception.ErrorCode;
import com.capstone.arfly.common.util.S3Uploader;
import com.capstone.arfly.jetson.dto.JetsonAlertCreateRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class JetsonAlertService {
    private final JetsonAlertWriter jetsonAlertWriter;
    private final S3Uploader s3Uploader;

    // 젯슨 이상 징후 업로드 (사진 S3 업로드 후 알림 저장)
    public void create(long deviceId, JetsonAlertCreateRequestDto request, MultipartFile image) {
        FileDetailDto fileDetail = s3Uploader.makeMetaData(image, S3DIRNAME.JETSON_ALERT_IMAGE.name());
        // 이상 징후 사진은 이미지만 허용
        if (fileDetail.getFileType() != FileType.IMAGE) {
            throw new BusinessException(ErrorCode.INVALID_FILE_TYPE);
        }

        s3Uploader.uploadFile(fileDetail.getKey(), image);
        try {
            jetsonAlertWriter.saveAlert(deviceId, request, fileDetail);
        } catch (DataAccessException e) {
            log.error("젯슨 이상 징후 저장 실패로 업로드된 사진을 정리합니다.", e);
            s3Uploader.deleteFile(fileDetail.getKey());
            throw new BusinessException(ErrorCode.JETSON_ALERT_SAVE_FAILED);
        }
    }
}
