package com.capstone.arfly.admin.controller;

import com.capstone.arfly.admin.dto.MemberDetailResponseDto;
import com.capstone.arfly.admin.dto.MemberListResponseDto;
import com.capstone.arfly.admin.service.AdminMemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/members")
public class AdminMemberController {
    private final AdminMemberService adminMemberService;

    @Operation(summary = "[관리자] 회원 목록 조회", description = "전체 회원 목록을 최근 가입 순으로 조회합니다. 정지·탈퇴 회원도 포함되며 status로 구분합니다.")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "조회 성공"),
                @ApiResponse(responseCode = "401", description = "인증 실패 (토큰 만료 혹은 유효하지 않은 토큰)"),
                @ApiResponse(responseCode = "403", description = "관리자 권한 없음")
            })
    @GetMapping
    public ResponseEntity<List<MemberListResponseDto>> getMembers() {
        return ResponseEntity.ok(adminMemberService.getMembers());
    }

    @Operation(summary = "[관리자] 회원 상세 조회", description = "회원의 기본 정보, 연락처, 게시글·댓글·반려동물 수와 작성한 게시글 목록을 조회합니다.")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "조회 성공"),
                @ApiResponse(responseCode = "401", description = "인증 실패 (토큰 만료 혹은 유효하지 않은 토큰)"),
                @ApiResponse(responseCode = "403", description = "관리자 권한 없음"),
                @ApiResponse(responseCode = "404", description = "존재하지 않는 회원")
            })
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberDetailResponseDto> getMemberDetail(@PathVariable Long memberId) {
        return ResponseEntity.ok(adminMemberService.getMemberDetail(memberId));
    }

    @Operation(summary = "[관리자] 회원 정지", description = "회원을 영구 정지합니다. 관리자 계정과 탈퇴한 회원은 정지할 수 없습니다.")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "204", description = "정지 성공"),
                @ApiResponse(responseCode = "400", description = "관리자 계정 정지 시도"),
                @ApiResponse(responseCode = "401", description = "인증 실패 (토큰 만료 혹은 유효하지 않은 토큰)"),
                @ApiResponse(responseCode = "403", description = "관리자 권한 없음"),
                @ApiResponse(responseCode = "404", description = "존재하지 않는 회원"),
                @ApiResponse(responseCode = "409", description = "이미 정지된 회원 또는 탈퇴한 회원")
            })
    @PatchMapping("/{memberId}/suspend")
    public ResponseEntity<Void> suspendMember(@PathVariable Long memberId) {
        adminMemberService.suspendMember(memberId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "[관리자] 회원 정지 해제", description = "정지된 회원을 정상 상태로 되돌립니다.")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "204", description = "정지 해제 성공"),
                @ApiResponse(responseCode = "401", description = "인증 실패 (토큰 만료 혹은 유효하지 않은 토큰)"),
                @ApiResponse(responseCode = "403", description = "관리자 권한 없음"),
                @ApiResponse(responseCode = "404", description = "존재하지 않는 회원"),
                @ApiResponse(responseCode = "409", description = "정지 상태가 아닌 회원")
            })
    @PatchMapping("/{memberId}/unsuspend")
    public ResponseEntity<Void> unsuspendMember(@PathVariable Long memberId) {
        adminMemberService.unsuspendMember(memberId);
        return ResponseEntity.noContent().build();
    }
}
