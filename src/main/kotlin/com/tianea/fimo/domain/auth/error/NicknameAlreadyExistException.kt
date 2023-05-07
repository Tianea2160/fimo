package com.tianea.fimo.domain.auth.error

import com.tianea.fimo.shared.error.FimoException

class NicknameAlreadyExistException : FimoException(
    code = "NICKNAME_ALREADY_EXIST",
    message = "Nickname already exist",
    status = 409
) {
}