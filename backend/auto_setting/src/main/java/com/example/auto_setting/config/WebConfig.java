package com.example.auto_setting.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")  // `/api` 경로에 대한 CORS 허용
                .allowedOrigins("http://3.209.94.202:8089")  // 프론트엔드 서버 주소 허용
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // 허용할 HTTP 메서드 지정
                .allowedHeaders("*")  // 모든 헤더 허용
                .allowCredentials(true);  // 쿠키를 포함한 인증 정보 허용 (필요 시)
    }
}
