package com.capstone.arfly.member.service;

import com.capstone.arfly.common.exception.BusinessException;
import com.capstone.arfly.common.exception.ErrorCode;
import com.capstone.arfly.community.repository.CommentRepository;
import com.capstone.arfly.community.repository.PostLikeRepository;
import com.capstone.arfly.community.repository.PostRepository;
import com.capstone.arfly.diagnosis.repository.DiagnosisReportRepository;
import com.capstone.arfly.member.domain.Member;
import com.capstone.arfly.member.domain.Role;
import com.capstone.arfly.member.dto.UpdateUserRequest;
import com.capstone.arfly.member.dto.UserIdCheckRequestDto;
import com.capstone.arfly.member.dto.UserNameCheckRequestDto;
import com.capstone.arfly.member.dto.UserProfileResponse;
import com.capstone.arfly.member.repository.MemberRepository;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final DiagnosisReportRepository diagnosisReportRepository;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentRepository commentRepository;
    private final GeoApiContext geoApiContext;

    @Transactional(readOnly = true)
    public boolean isUsernameAvailable(UserNameCheckRequestDto userNameCheckRequestDto) {
        Optional<Member> member = memberRepository.findByNickName(userNameCheckRequestDto.getNickname());
        if (member.isPresent()) {
            return false;
        }
        return true;
    }

    @Transactional(readOnly = true)
    public boolean isIdAvailable(UserIdCheckRequestDto userIdCheckRequestDto) {
        Optional<Member> member = memberRepository.findByUserId(userIdCheckRequestDto.getUserId());
        if (member.isPresent()) {
            return false;
        }
        return true;
    }

    // 내 프로필 정보 조회
    @Transactional(readOnly = true)
    public UserProfileResponse getMyProfile(Long memberId) {

        Member member =
                memberRepository.findById(memberId).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_EXISTS));

        boolean isDoctor = Role.DOCTOR.equals(member.getRole());

        Long diagnosisCounts = diagnosisReportRepository.countByMember(member);

        Long postCounts = postRepository.countByMember(member);

        Long commentCounts = commentRepository.countByMember(member);

        Long likeCounts = postLikeRepository.countByMember(member);

        return UserProfileResponse.builder()
                .userId(member.getId())
                .nickname(member.getNickName())
                .doctor(isDoctor)
                .notificationEnabled(member.isNotificationEnabled())
                .phoneNumber(member.getPhoneNumber())
                .latitude(member.getLatitude())
                .longitude(member.getLongitude())
                .roadAddress(member.getRoad_address())
                .diagnosisCounts(diagnosisCounts)
                .postCounts(postCounts)
                .commentCounts(commentCounts)
                .likeCounts(likeCounts)
                .build();
    }

    // 내 정보 수정
    @Transactional
    public void updateMyProfile(Long memberId, UpdateUserRequest request) {

        Member member =
                memberRepository.findById(memberId).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_EXISTS));

        double latitude = member.getLatitude();
        double longitude = member.getLongitude();

        if (request.getRoadAddress() != null && !request.getRoadAddress().isBlank()) {
            double[] location = getLatLngFromAddress(request.getRoadAddress());
            latitude = location[0];
            longitude = location[1];
        }

        member.updateProfile(
                request.getNickname(), latitude, longitude, request.getRoadAddress(), request.isNotificationEnabled());

        String rawPassword = request.getPassword();
        if (rawPassword != null && !rawPassword.isBlank()) {
            String encodedPassword = passwordEncoder.encode(rawPassword);
            member.updatePassword(encodedPassword);
        }
    }

    // 도로명 주소를 위도 경도로 반환
    private double[] getLatLngFromAddress(String roadAddress) {
        try {
            GeocodingResult[] results =
                    GeocodingApi.geocode(geoApiContext, roadAddress).await();

            if (results == null || results.length == 0) {
                throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
            }

            double latitude = results[0].geometry.location.lat;
            double longitude = results[0].geometry.location.lng;

            return new double[] {latitude, longitude};
        } catch (ApiException | IOException | InterruptedException e) {
            log.error("주소 지오코딩 실패: roadAddress={}", roadAddress, e);
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            throw new BusinessException(ErrorCode.INVALID_ADDRESS_VALUE);
        }
    }
}
