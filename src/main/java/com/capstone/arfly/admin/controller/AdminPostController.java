package com.capstone.arfly.admin.controller;

import com.capstone.arfly.community.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/posts")
public class AdminPostController {
    private final PostService postService;

    @Operation(summary = "[관리자] 게시글 삭제", description = "작성자와 관계없이 게시글을 삭제합니다. 게시글의 댓글, 좋아요, 이미지, 신고 내역도 함께 삭제됩니다.")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "204", description = "삭제 성공"),
                @ApiResponse(responseCode = "401", description = "인증 실패 (토큰 만료 혹은 유효하지 않은 토큰)"),
                @ApiResponse(responseCode = "403", description = "관리자 권한 없음"),
                @ApiResponse(responseCode = "404", description = "존재하지 않는 게시글")
            })
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePostByAdmin(postId);
        return ResponseEntity.noContent().build();
    }
}
