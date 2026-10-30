package com.capstone.arfly.jetson.auth;

import com.capstone.arfly.common.exception.ErrorCode;
import com.capstone.arfly.common.exception.ErrorResponse;
import com.capstone.arfly.jetson.domain.JetsonDevice;
import com.capstone.arfly.jetson.repository.JetsonDeviceRepository;
import com.capstone.arfly.jetson.util.JetsonTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.json.JsonMapper;

// 기기 토큰을 해시해서 기기를 찾고, principal에 기기 ID(Long)와 ROLE_JETSON 권한을 넣음
@RequiredArgsConstructor
public class JetsonDeviceTokenFilter extends OncePerRequestFilter {
    public static final String DEVICE_PATH_PREFIX = "/api/jetson-device/";

    private final JetsonDeviceRepository jetsonDeviceRepository;
    private final JsonMapper jsonMapper;

    public static boolean isDeviceRequest(HttpServletRequest request) {
        return request.getRequestURI().startsWith(DEVICE_PATH_PREFIX);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !isDeviceRequest(request);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header == null
                || !header.startsWith("Bearer ")
                || header.substring(7).isBlank()) {
            writeUnauthorized(response);
            return;
        }

        Optional<JetsonDevice> device =
                jetsonDeviceRepository.findByTokenHash(JetsonTokenUtil.hash(header.substring(7)));
        if (device.isEmpty()) {
            writeUnauthorized(response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                device.get().getId(), null, List.of(new SimpleGrantedAuthority("ROLE_JETSON")));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    private void writeUnauthorized(HttpServletResponse response) throws IOException {
        ErrorCode errorCode = ErrorCode.INVALID_JETSON_TOKEN;
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter()
                .write(jsonMapper.writeValueAsString(
                        ErrorResponse.of(errorCode.getErrorCode(), errorCode.getMessage())));
    }
}
