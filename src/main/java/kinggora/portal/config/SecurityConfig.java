package kinggora.portal.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import kinggora.portal.security.auth.CustomUsernamePasswordAuthenticationFilter;
import kinggora.portal.security.auth.JwtAuthenticationFilter;
import kinggora.portal.security.auth.SignInFailureHandler;
import kinggora.portal.security.auth.SignInSuccessJwtProvideHandler;
import kinggora.portal.security.exception.handler.CustomAccessDeniedHandler;
import kinggora.portal.security.exception.handler.CustomAuthenticationEntryPoint;
import kinggora.portal.security.exception.handler.JwtAuthExceptionHandlingFilter;
import kinggora.portal.util.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Spring Security 설정
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;

    /**
     * Spring Security Filter Chain 빈 등록
     * 인증 방식, 세션 정책, HTTP 요청 인가 설정, 스프링 시큐리티 예외 핸들러, 필터 적용 순서 등을 정의
     *
     * @param http HTTP 요청 보안 설정
     * @return SecurityFilterChain
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .formLogin().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .authorizeHttpRequests()
                .mvcMatchers(HttpMethod.GET, "/download/**").hasAuthority("ROLE_USER")
                .mvcMatchers(HttpMethod.POST, "/members").permitAll()
                .mvcMatchers("/members").hasAuthority("ROLE_USER")
                .mvcMatchers("/**").permitAll()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler())
                .authenticationEntryPoint(authenticationEntryPoint())
                .and()
                .addFilterBefore(customUsernamePasswordAuthenticationFilter(), LogoutFilter.class)
                .addFilterBefore(jwtAuthenticationFilter(), CustomUsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionHandlingFilter(), JwtAuthenticationFilter.class);
        return http.build();
    }

    /**
     * Authentication Processing Filter 빈 등록
     * 인증을 수행할 AuthenticationManager, 인증 성공/실패 핸들러 설정
     * HTTP 로그인 요청에서 인증 정보를 추출하여 AuthenticationManager 호출
     *
     * @return CustomUsernamePasswordAuthenticationFilter
     */
    @Bean
    public CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter() {
        CustomUsernamePasswordAuthenticationFilter authenticationFilter = new CustomUsernamePasswordAuthenticationFilter(authenticationManager());
        authenticationFilter.setAuthenticationManager(authenticationManager());
        authenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
        authenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler());
        return authenticationFilter;
    }

    /**
     * 인증 성공 핸들러 빈 등록
     *
     * @return SignInSuccessJwtProvideHandler
     */
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new SignInSuccessJwtProvideHandler(jwtProvider, objectMapper);
    }

    /**
     * 인증 실패 핸들러 빈 등록
     *
     * @return SignInFailureHandler
     */
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new SignInFailureHandler(objectMapper);
    }

    /**
     * password 단방향 암호화기 빈 등록
     * Spring Security 가 제공하는 PasswordEncoder 사용 (위임)
     * (기본 설정: BCryptPasswordEncoder)
     *
     * @return BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * UsernamePasswordAuthenticationFilter 의 호출로 실제 인증을 수행할 AuthenticationManager 빈 등록
     * CustomUsernamePasswordAuthenticationFilter 로부터 UsernamePasswordAuthenticationToken 을 받아 인증 수행
     * 1. UserDetailsService.loadUserByUsername(username): UserDetail 조회
     * 2. PasswordEncoder.match(password, UserDetail.password): password 인증
     *
     * @return DaoAuthenticationProvider
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return new ProviderManager(provider);
    }

    /**
     * 스프링 시큐리티 예외: AuthenticationException 처리 핸들러 빈 등록
     * 리소스(URL) 요청 중 발생한 인증 관련 예외 처리
     *
     * @return CustomAuthenticationEntryPoint
     */
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint(objectMapper);
    }

    /**
     * 스프링 시큐리티 예외: AccessDeniedException 처리 핸들러 빈 등록
     * 리소스(URL) 요청 중 발생한 인가 관련 예외 처리
     *
     * @return CustomAccessDeniedHandler
     */
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler(objectMapper);
    }

    /**
     * JWT 기반 인증 필터 빈 등록
     * HTTP 요청에 Authorization 헤더가 있는 경우 인증 수행
     *
     * @return JwtAuthenticationFilter
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtProvider, userDetailsService);
    }

    /**
     * JwtAuthenticationFilter에서 발생한 JwtException 핸들러 필터 빈 등록
     *
     * @return JwtExceptionHandlingFilter
     */
    @Bean
    public JwtAuthExceptionHandlingFilter jwtExceptionHandlingFilter() {
        return new JwtAuthExceptionHandlingFilter(objectMapper);
    }

    /**
     * 개발 단계의 CORS 설정
     * 자원 공유를 허용할 출처 origin, request methods 등
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of("http://localhost:8081"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Content-Disposition"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
