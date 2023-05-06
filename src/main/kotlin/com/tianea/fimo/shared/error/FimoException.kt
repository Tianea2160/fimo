package com.tianea.fimo.shared.error

open class FimoException(
    message: String,
    code: String,
    status: Int
) : RuntimeException() {
    val errorCode = ErrorCode(code, message, status)
}

class ErrorCode(
    val code: String,
    val message: String,
    val status: Int
)