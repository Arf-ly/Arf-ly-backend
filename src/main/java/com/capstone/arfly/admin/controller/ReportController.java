package com.capstone.arfly.admin.controller;

import com.capstone.arfly.admin.dto.ReportCreateRequestDto;
import com.capstone.arfly.admin.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @Operation(summary = "게시글 신고", description = "게시글을 신고합니다. 같은 게시글은 한 번만 신고할 수 있으며, 기타(ETC) 사유는 상세 사유가 필요합니다.")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "201", description = "신고 성공"),
                @ApiResponse(responseCode = "400", description = "사유 누락, 본인 게시글 신고, 기타 사유인데 상세 사유 누락"),
                @ApiResponse(responseCode = "401", description = "인증 실패 (토큰 만료 혹은 유효하지 않은 토큰)"),
                @ApiResponse(responseCode = "404", description = "존재하지 않는 게시글"),
                @ApiResponse(responseCode = "409", description = "이미 신고한 게시글")
            })
    @PostMapping("/api/posts/{postId}/reports")
    public ResponseEntity<?> reportPost(
            @PathVariable Long postId,
            @Valid @RequestBody ReportCreateRequestDto requestDto,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails) {
        long reporterId = Long.parseLong(userDetails.getUsername());
        reportService.reportPost(postId, reporterId, requestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
