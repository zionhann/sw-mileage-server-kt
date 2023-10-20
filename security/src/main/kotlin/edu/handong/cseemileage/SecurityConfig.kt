package edu.handong.cseemileage

import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.RequestMatcher

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val tokenProvider: TokenProvider
) {
    @Bean
    fun passwordEncoder(): PasswordEncoder? {
        return BCryptPasswordEncoder()
    }

    @Throws(Exception::class)
    @Bean
    fun filterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        httpSecurity
            // JWT 토큰 기반의 인증을 사용하기 위해 CSRF 토큰 검증 비활성화
            .csrf().disable()
            .httpBasic().disable() // JWT 토큰 기반의 인증을 사용하기 위해 HTTP basic 인증 비활성화
            .formLogin().disable() // 스프링 시큐리티의 기본 폼 로그인 비활성화
            .addFilterBefore(JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter::class.java) // JWT 인증 필터를 UsernamePasswordAuthenticationFilter 전에 넣음
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 서버 리소스 절약, JWT 사용해 매 요청마다 인증 처리
            .and()
            .authorizeHttpRequests() // HttpServletRequest를 사용하는 요청들에 대한 접근 제한 설정
            .requestMatchers(PathRequest.toH2Console()).permitAll() // h2-console 요청 인증 무시 - 테스트용
            .requestMatchers(RequestMatcher { "/api/student/login" == it.servletPath }).permitAll() // 학생 로그인 api
            .requestMatchers(RequestMatcher { "/api/admin/login" == it.servletPath }).permitAll() // 관리자 로그인 api
            // TODO: 학생, 관리자 A-B-C-D 각각 접근 가능한 api 설정하기
            .antMatchers("/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs/**").permitAll()
            .requestMatchers(RequestMatcher { "/favicon.ico" == it.servletPath }).permitAll()
            .anyRequest().authenticated() // 그 외 인증 없이 접근X
            .and()
            .headers()
            .frameOptions().sameOrigin() // X-Frame-Options 추가 -> Clickjacking 공격 방지

        return httpSecurity.build()
    }

    @Bean
    fun webSecurityCustomizer(): WebSecurityCustomizer? {
        return WebSecurityCustomizer { web: WebSecurity ->
            // CSS, JavaScript, 이미지 파일 등의 정적 리소스를 제공할 때 스프링 시큐리티를 통한 인증을 거치지 않는다.
            web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations())
        }
    }
}
