package com.ll.exam.FinalProject_KimArum.app.security;


import com.ll.exam.FinalProject_KimArum.app.jwt.filter.JwtAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthorizationFilter jwtAuthorizationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(
                        authorizeRequests -> authorizeRequests
                                .antMatchers("/api/v1/member/login", "member/join", "member/login")
                                .permitAll()
                                .anyRequest()
                                .authenticated() // 최소자격 : 로그인
                )
                .cors().disable() // 타 도메인에서 API 호출 가능
                .csrf().disable() // CSRF 토큰 끄기
                .httpBasic().disable() // httpBaic 로그인 방식 끄기
                .formLogin().disable() // 폼 로그인 방식 끄기
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(STATELESS)
                ).addFilterBefore(
                        jwtAuthorizationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );
//                .formLogin(
//                        formLogin -> formLogin
//                                .loginPage("/member/login") // GET
//                                .loginProcessingUrl("/member/login") // POST
//                                .successHandler(authenticationSuccessHandler)
//                                .failureHandler(authenticationFailureHandler)
//                )
//                .logout(
//                        logout -> logout
//                                .logoutUrl("/member/logout")
//                );

        return http.build();
    }
}
