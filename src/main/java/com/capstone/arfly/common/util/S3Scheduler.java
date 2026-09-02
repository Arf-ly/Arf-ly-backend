package com.capstone.arfly.common.util;

import com.capstone.arfly.common.exception.BusinessException;
import com.capstone.arfly.common.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class S3Scheduler {
    private final FileRepository fileRepository;
    private final S3Uploader s3Uploader;

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void deleteFiles() {
        log.info("Deleting files start");
        fileRepository.findAllByDeletedTrue().forEach(file -> {
            try {
                s3Uploader.deleteFile(file.getFileKey());
                fileRepository.delete(file);
            } catch (BusinessException e) {
                log.error("scheduler deletefile s3 error key: {} ", file.getFileKey());
                // CHECKSTYLE:OFF: IllegalCatch -- 파일 하나가 실패해도 나머지 파일 삭제는 계속 진행되어야 함
            } catch (Exception e) {
                log.error("Scheduler deletefile database error: {}", file.getFileKey(), e);
            }
            // CHECKSTYLE:ON: IllegalCatch
        });
        log.info("Deleting files end");
    }
}
