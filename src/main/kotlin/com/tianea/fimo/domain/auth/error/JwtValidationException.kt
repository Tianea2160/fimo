package com.tianea.fimo.domain.auth.error

import com.tianea.fimo.shared.error.FimoException

class JwtValidationException : FimoException(
    code = "JWT_VALIDATION_ERROR",
    message = "JWT validation error",
    status = 400
)