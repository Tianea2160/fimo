package com.tianea.fimo.domain.auth.error

import com.tianea.fimo.shared.error.FimoException

class ArchiveAlreadyExistException : FimoException(
    code = "ARCHIVE_ALREADY_EXIST",
    message = "Archive already exist",
    status = 409
) {
}