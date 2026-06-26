package com.capstone.arfly.common.config;

import com.capstone.arfly.common.auth.TermsAgreementInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TermsAgreementInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/auth/**",
                        "/member/check-username",
                        "/member/check-userId",
                        "/notification/token",
                        "/terms/oauth/agree",
                        "/terms/latest/agreement-status",
                        "/terms/latest",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/swagger-ui.html"
                );
    }
}
