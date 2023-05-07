package com.tianea.fimo.domain.auth.error

import com.tianea.fimo.shared.error.FimoException

class UserAlreadyExistException  : FimoException(
    code = "USER_ALREADY_EXIST",
    message = "User already exist",
    status = 409
)