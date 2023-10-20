package edu.handong.cseemileage.controller

import edu.handong.cseemileage.AuthService
import edu.handong.cseemileage.JwtFilter
import edu.handong.cseemileage.LoginForm
import edu.handong.cseemileage.TokenDto
import edu.handong.cseemileage.TokenProvider
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class AuthController(
    private val tokenProvider: TokenProvider,
    private val authService: AuthService
) {
    @PostMapping("/student/login")
    @Parameter(
        name = "Authentication",
        example = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdHJpbmciLCJhdXRob3JpdGllcyI6IlJPTEVfQURNSU5fRCxST0xFX0FETUlOX0MsUk9MRV9BRE1JTl9CLFJPTEVfQURNSU5fQSIsImV4cCI6MTcwMTUwMjQ4OCwiaWF0IjoxNjk2MzE4NDg4fQ.LnWCsVUPPtrPRtUwOUZgqiCtDCd3j3pbw0G1-Ht1v8Kpl54VVTUmcVzw0dVJnm9iTTJ_ZJzYK1PhqThCMJmRAw",
        required = true,
        `in` = ParameterIn.HEADER
    )
    fun login(
        @RequestBody form: LoginForm
    ): ResponseEntity<TokenDto> {
        val jwt = authService.studentLogin(form)
        return ResponseEntity(responseHeader(jwt), HttpStatus.CREATED)
    }

    @PostMapping("/admin/login")
    @Parameter(
        name = "Authentication",
        example = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdHJpbmciLCJhdXRob3JpdGllcyI6IlJPTEVfQURNSU5fRCxST0xFX0FETUlOX0MsUk9MRV9BRE1JTl9CLFJPTEVfQURNSU5fQSIsImV4cCI6MTcwMTUwMjQ4OCwiaWF0IjoxNjk2MzE4NDg4fQ.LnWCsVUPPtrPRtUwOUZgqiCtDCd3j3pbw0G1-Ht1v8Kpl54VVTUmcVzw0dVJnm9iTTJ_ZJzYK1PhqThCMJmRAw",
        required = true,
        `in` = ParameterIn.HEADER
    )
    fun loginAdmin(
        @RequestBody form: LoginForm
    ): ResponseEntity<TokenDto> {
        val jwt = authService.adminLogin(form)
        return ResponseEntity(responseHeader(jwt), HttpStatus.CREATED)
    }

    private fun responseHeader(jwt: String): HttpHeaders {
        val httpHeaders = HttpHeaders()
        httpHeaders.add(JwtFilter.BEARER, "$jwt")
        httpHeaders.add("Level", tokenProvider.getAuthentication(jwt).authorities.joinToString { it.authority })
        return httpHeaders
    }
}
