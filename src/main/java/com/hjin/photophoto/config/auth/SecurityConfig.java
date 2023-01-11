package com.hjin.photophoto.config.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity

public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .authorizeRequests()
                    .antMatchers("/user/**").authenticated() //접근 시 인증이 필요한 url 설정
                    .anyRequest().permitAll() //위 url 빼고 전부 인가
                .and()
                .formLogin() //아이디 비번
                    .loginPage("/signin") //로그인 필요할 때 redirect 시킬 페이지
                    .loginProcessingUrl("/signinProc")//여기로 id,pw 주면 자동 로그인됨
                    .defaultSuccessUrl("/") //로그인 성공 시 기본 url
                    .failureUrl("/") //로그인 실패시 url
                .and().build();
    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}