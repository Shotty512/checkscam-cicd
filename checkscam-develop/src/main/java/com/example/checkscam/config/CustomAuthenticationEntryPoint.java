package com.example.checkscam.config;

import com.example.checkscam.constant.ErrorCodeEnum;
import com.example.checkscam.response.CheckScamResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

import static com.example.checkscam.constant.ErrorCodeEnum.INVALID_REQUEST;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final AuthenticationEntryPoint delegate = new BearerTokenAuthenticationEntryPoint();

    private final ObjectMapper mapper;

    public CustomAuthenticationEntryPoint(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
        response.setContentType("application/json;charset=UTF-8");

        // Tạo thông báo lỗi từ nguyên nhân gốc nếu có
        String errorMessage = Optional.ofNullable(authException.getCause())
                .map(Throwable::getMessage)
                .orElse(authException.getMessage());

        // Tạo response trả về dạng JSON sử dụng class CheckScamResponse
        CheckScamResponse<Object> res = new CheckScamResponse<>(
                ErrorCodeEnum.UNAUTHORIZED  // dùng enum để lấy code và message
        );

        // Ghi response ra output
        mapper.writeValue(response.getWriter(), res);
    }

}
