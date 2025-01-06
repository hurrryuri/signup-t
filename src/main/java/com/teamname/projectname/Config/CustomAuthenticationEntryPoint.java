package com.teamname.projectname.Config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.naming.AuthenticationException;
import java.io.IOException;

//검증 실패했을 때 사용자 클래스
//생략 가능(필요할 때만 사용)
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.AuthenticationException authException) throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "인증 실패!");
    }
}
