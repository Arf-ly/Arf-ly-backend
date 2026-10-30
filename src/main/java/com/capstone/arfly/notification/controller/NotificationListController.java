package com.capstone.arfly.notification.controller;

import com.capstone.arfly.notification.dto.NotificationListResponseDto;
import com.capstone.arfly.notification.service.NotificationListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "notification", description = "알림 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationListController {
    private final NotificationListService notificationListService;

    @Operation(
            summary = "알림 목록 조회 (무한 스크롤)",
            description = "내가 받은 알림(댓글 언급, 젯슨 이상 징후)을 최신순으로 조회합니다. 이상 징후 사진 URL은 10분간 유효합니다.")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "조회 성공"),
                @ApiResponse(responseCode = "401", description = "인증 실패 (토큰 만료 혹은 유효하지 않은 토큰)")
            })
    @GetMapping
    public ResponseEntity<NotificationListResponseDto> getNotifications(
            @Parameter(description = "마지막으로 조회한 알림 ID (첫 페이지 조회 시에는 비워두세요)", example = "12")
                    @RequestParam(required = false)
                    Long cursor,
            @Parameter(description = "한 번에 조회할 알림 개수", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails) {
        Long memberId = Long.parseLong(userDetails.getUsername());
        return ResponseEntity.ok(notificationListService.getNotifications(memberId, cursor, size));
    }
}
