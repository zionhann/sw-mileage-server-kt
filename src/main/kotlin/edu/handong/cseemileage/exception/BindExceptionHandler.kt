package edu.handong.cseemileage.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindException
import org.springframework.validation.FieldError
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class BindExceptionHandler {

    @ExceptionHandler(BindException::class)
    fun handleBindException(e: BindException): ResponseEntity<ExceptionResponse> {
        val res: FieldError? = e.bindingResult.fieldError

        return ResponseEntity.badRequest()
            .body(
                ExceptionResponse(
                    status = HttpStatus.BAD_REQUEST.reasonPhrase,
                    message = res?.defaultMessage,
                    info = e.message
                )
            )
    }
}
