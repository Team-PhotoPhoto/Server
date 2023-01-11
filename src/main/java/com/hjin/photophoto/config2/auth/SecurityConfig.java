package com.hjin.photophoto.config2.auth;

import com.hjin.photophoto.domain.auth.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable().headers().frameOptions().disable()// h2콘솔 사용 용
                .and()
                .authorizeRequests()
                .antMatchers("/", "/css/**", "/images/**", "/js/**").permitAll()  // 권한 관리 대상, 전체 열람 가능
                .antMatchers("/api/v1/**").hasRole(Role.USER.name())    //USER 권한을 가진 사람만 이 api 사용 가능
                .anyRequest().authenticated()   //위에 설정된 url 이외 나머지들.인증된(로그인) 사용자들에게만 허용
                .and()
                .formLogin()
                .loginPage("/signin")
                .logout()
                .logoutSuccessUrl("/signout")
                .and()
                .oauth2Login()
                .userInfoEndpoint()// 로그인 성공 이후 사용자 정보 가져올 때
                .userService(customOAuth2UserService); //소셜 로그인 성공 후 인터페이스 구현체 등록(ex. sns에서 가져오고 싶은 사용자 정보 기능 명시 가능
    }
}
