package com.hjin.photophoto.config.auth.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.rmi.ServerException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    public static final String AUTHORIZATION_HAEDER = "Authorization";
    public static final String BEARER_PREFIX = "bearer ";

    private final JwtUtil jwtUtil;

    // JWT의 인증 정보를 현재 쓰레드의 SecurityContext에 저장
    // authentication > SecurityContext > ContextHolder

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {

        String jwt = resolveToken(request);
//        System.out.println(jwt);
        jwtUtil.isValidToken(jwt);

        filterChain.doFilter(request, response);

    }

    // request header 에서 토큰 꺼내오는 메소드
    private String resolveToken(HttpServletRequest request) {
        String bearToken = request.getHeader(AUTHORIZATION_HAEDER);
//        System.out.println("bearToken: "+bearToken);
        if(StringUtils.hasText(bearToken) && bearToken.startsWith(BEARER_PREFIX)) {
            System.out.println("bearToken: "+bearToken.substring(7));
            return bearToken.substring(7);
        }
        return null;
    }


}
