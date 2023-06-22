package com.tianea.fimo.domain.user.error

import com.tianea.fimo.shared.error.FimoException


class UserNotFoundException : FimoException(
    message = "User not found",
    code = "USER_NOT_FOUND",
    status = 400
)