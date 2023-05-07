package com.tianea.fimo.shared.error

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice


@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(FimoException::class)
    fun fimoException(exception: FimoException): ResponseEntity<ErrorCode> =
        ResponseEntity.status(exception.errorCode.status).body(exception.errorCode)
}
