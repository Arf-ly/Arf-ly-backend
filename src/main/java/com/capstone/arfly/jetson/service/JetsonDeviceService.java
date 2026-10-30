package com.capstone.arfly.jetson.service;

import com.capstone.arfly.common.exception.BusinessException;
import com.capstone.arfly.common.exception.ErrorCode;
import com.capstone.arfly.jetson.domain.JetsonDevice;
import com.capstone.arfly.jetson.dto.JetsonDeviceRegisterResponseDto;
import com.capstone.arfly.jetson.repository.JetsonDeviceRepository;
import com.capstone.arfly.jetson.util.JetsonTokenUtil;
import com.capstone.arfly.member.domain.Member;
import com.capstone.arfly.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JetsonDeviceService {
    private final JetsonDeviceRepository jetsonDeviceRepository;
    private final MemberRepository memberRepository;

    // 젯슨 기기 등록 후 기기 토큰 발급 (원문 토큰은 응답으로 한 번만 반환)
    @Transactional
    public JetsonDeviceRegisterResponseDto register(long memberId, String name) {
        Member member =
                memberRepository.findById(memberId).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_EXISTS));

        String token = JetsonTokenUtil.generateToken();
        JetsonDevice device = jetsonDeviceRepository.save(JetsonDevice.builder()
                .owner(member)
                .name(name)
                .tokenHash(JetsonTokenUtil.hash(token))
                .build());

        return JetsonDeviceRegisterResponseDto.builder()
                .deviceId(device.getId())
                .token(token)
                .build();
    }
}
