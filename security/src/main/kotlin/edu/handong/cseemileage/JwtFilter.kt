package edu.handong.cseemileage

import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtFilter(
    private val tokenProvider: TokenProvider
) : OncePerRequestFilter() {

    companion object {
        const val BEARER = "Bearer "
    }

    @Throws(IOException::class, ServletException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = extractToken(request)
        tokenProvider
            .validateToken(token)
            .ifPresent { claims ->
                val authorities = claims["authorities"].toString().split(",")
                val principal = User(claims.subject, "", authorities.map { SimpleGrantedAuthority(it) })
                val authn: Authentication = UsernamePasswordAuthenticationToken(principal, token, emptyList())
                SecurityContextHolder.getContext().authentication = authn
            }
        filterChain.doFilter(request, response)
    }

    private fun extractToken(request: HttpServletRequest): Optional<String> {
        return Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
            .filter { value -> value.startsWith(BEARER) }
            .map { value -> value.substring(BEARER.length) }
    }
}
