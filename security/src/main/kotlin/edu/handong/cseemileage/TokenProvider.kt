package edu.handong.cseemileage

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import java.security.Key
import java.util.Date
import java.util.Optional

@Component
class TokenProvider(
    @Value("\${spring.jwt.secret}") private val secret: String,
    @Value("\${spring.jwt.token-validity-in-minutes}") private val tokenValidityInMinutes: Long
) : InitializingBean {

    private lateinit var key: Key
    override fun afterPropertiesSet() {
        val keyBytes = Decoders.BASE64.decode(secret)
        key = Keys.hmacShaKeyFor(keyBytes)
    }

    fun createStudentToken(authentication: Authentication): String {
        val now = Date()
        val validity = Date(now.time + tokenValidityInMinutes * 1000 * 60)

        val authorities = listOf("ROLE_STUDENT")
        return Jwts.builder()
            .setSubject(authentication.name)
            .claim("authorities", authorities.joinToString(","))
            .setExpiration(validity)
            .setIssuedAt(now)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()
    }

    fun createAdminToken(authentication: Authentication, level: Int): String {
        val now = Date()
        val validity = Date(now.time + tokenValidityInMinutes * 1000 * 60)

        val authorities = mutableListOf("ROLE_ADMIN_D")
        if (level <= 2) authorities.add("ROLE_ADMIN_C")
        if (level <= 1) authorities.add("ROLE_ADMIN_B")
        if (level == 0) authorities.add("ROLE_ADMIN_A")
        return Jwts.builder()
            .setSubject(authentication.name)
            .claim("authorities", authorities.joinToString(","))
            .setExpiration(validity)
            .setIssuedAt(now)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()
    }

    fun getAuthentication(token: String): Authentication {
        val claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body

        // 클레임에서 주체(subject) 정보를 추출하여 User 객체를 생성
        val principal = User(claims.subject, "", claims["authorities"].toString().split(",").map { SimpleGrantedAuthority(it) })

        return UsernamePasswordAuthenticationToken(principal, token, principal.authorities)
    }

    // 토큰의 유효성 검증을 수행하고 결과를 반환
    fun validateToken(token: Optional<String>): Optional<Claims> {
        try {
            return Optional.of(token)
                .filter { it.isPresent }
                .map { jwt ->
                    Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(jwt.get())
                        .body
                }
        } catch (e: JwtException) { }
        return Optional.empty()
    }

    fun getExpiration(token: String): Long {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
            .expiration
            .time
    }
}
