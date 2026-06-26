package com.capstone.arfly.common.auth;

import com.capstone.arfly.common.exception.TermsNotAgreedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
public class TermsAgreementInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Boolean termsAgreed = (Boolean) ((UsernamePasswordAuthenticationToken) authentication).getDetails();
        if (termsAgreed == null || !termsAgreed) {
            throw new TermsNotAgreedException();
        }
        return true;
    }
}
