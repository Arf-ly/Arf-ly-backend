package com.capstone.arfly.walkRecord.controller;

import com.capstone.arfly.common.exception.ErrorResponse;
import com.capstone.arfly.walkRecord.dto.AssignPetRequest;
import com.capstone.arfly.walkRecord.dto.AssignedWalkListResponse;
import com.capstone.arfly.walkRecord.dto.UnassignedWalkListResponse;
import com.capstone.arfly.walkRecord.service.WalkRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "walk", description = "산책 기록 API")
@RestController
@RequestMapping("/api/walks")
@RequiredArgsConstructor
public class WalkRecordController {

    private final WalkRecordService walkRecordService;

    @Operation(summary = "미배정 산책 기록 목록 조회",
            description = "JWT에서 userId를 추출하여 해당 유저의 미배정(petId = null) 산책 기록 전체를 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = UnassignedWalkListResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패 (토큰 만료 또는 유효하지 않음)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/unassigned")
    public ResponseEntity<UnassignedWalkListResponse> getUnassignedWalks(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails) {

        Long memberId = Long.parseLong(userDetails.getUsername());
        return ResponseEntity.ok(walkRecordService.getUnassignedWalks(memberId));
    }

    @Operation(summary = "반려동물 산책 기록 배정",
            description = "산책 기록을 특정 반려 동물에 배정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "배정 성공",
                    content = @Content(schema = @Schema(example = "{\"result\": \"ok\"}"))),
            @ApiResponse(responseCode = "403", description = "접근 권한 없음 (내 산책 기록 또는 내 반려동물이 아님)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "산책 기록 또는 반려동물을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/{walkId}/assign")
    public ResponseEntity<?> assignPet(
            @Parameter(description = "배정할 산책 기록 ID", required = true) @PathVariable Long walkId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "배정할 반려동물 ID",
                    required = true,
                    content = @Content(schema = @Schema(implementation = AssignPetRequest.class))
            )
            @RequestBody AssignPetRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails) {

        Long memberId = Long.parseLong(userDetails.getUsername());
        walkRecordService.assignPet(memberId, walkId, request.getPetId());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "반려동물 산책 기록 수정 (동물 변경)",
            description = "이미 배정된 산책 기록의 반려동물만 변경합니다. 다른 필드는 수정 불가.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "변경 성공",
                    content = @Content(schema = @Schema(example = "{\"result\": \"ok\"}"))),
            @ApiResponse(responseCode = "403", description = "접근 권한 없음 (내 산책 기록 또는 내 반려동물이 아님)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "산책 기록 또는 반려동물을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/{walkId}/pet")
    public ResponseEntity<?> reassignPet(
            @Parameter(description = "수정할 산책 기록 ID", required = true) @PathVariable Long walkId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "변경할 반려동물 ID",
                    required = true,
                    content = @Content(schema = @Schema(implementation = AssignPetRequest.class))
            )
            @RequestBody AssignPetRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails) {

        Long memberId = Long.parseLong(userDetails.getUsername());
        walkRecordService.reassignPet(memberId, walkId, request.getPetId());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "배정된 산책 기록 목록 조회",
            description = "petId 쿼리 파라미터가 없으면 전체 배정 기록, 있으면 해당 반려동물 기록만 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = AssignedWalkListResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패 (토큰 만료 또는 유효하지 않음)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<AssignedWalkListResponse> getAssignedWalks(
            @Parameter(description = "반려동물 ID (없으면 전체 배정 기록 반환)") @RequestParam(required = false) Long petId,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails) {

        Long memberId = Long.parseLong(userDetails.getUsername());
        return ResponseEntity.ok(walkRecordService.getAssignedWalks(memberId, petId));
    }
}
