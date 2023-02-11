package com.hjin.photophoto.config.auth;

import com.hjin.photophoto.config.auth.jwt.JwtAuthenticationFilter;
import com.hjin.photophoto.config.auth.jwt.JwtUtil;
import com.hjin.photophoto.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final LoginHandler loginHandler;
    private final JwtUtil jwtUtil;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                    .csrf().disable().headers().frameOptions().disable()// h2콘솔 사용 용
                .and()
                    .cors()
                .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //세션 사용 안 함
                .and()
                    .authorizeRequests()
                    .anyRequest().permitAll()   //위에 설정된 url 이외 나머지들. 모두 허용
                .and()
                    .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/")
                    .invalidateHttpSession(true).deleteCookies("JSESSIONID")
                .and()
                    .addFilterBefore(new JwtAuthenticationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
                    .oauth2Login()
                    .successHandler(loginHandler)
                    .userInfoEndpoint()// 로그인 성공 이후 사용자 정보 가져올 때
                    .userService(customOAuth2UserService); //소셜 로그인 성공 후 인터페이스 구현체 등록(ex. sns에서 가져오고 싶은 사용자 정보 기능 명시 가능
    }

//
//                    .antMatchers("/profile/signup").hasRole(Role.JOIN.name())    //JOIN 권한을 가진 사람만 이 api 사용 가능
//            .antMatchers("/inbox", "/gallery/me", "/profile/me").hasRole(Role.JOIN.name())

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:3000/"));
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;

    }
}
