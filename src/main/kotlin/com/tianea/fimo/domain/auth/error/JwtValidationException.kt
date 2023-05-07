package com.tianea.fimo.domain.auth.error

import com.tianea.fimo.shared.error.FimoException

class JwtValidationException : FimoException(
    code = "jwt_validation_error",
    message = "JWT validation error",
    status = 400
) {
}