package com.tianea.fimo.domain.post.error

import com.tianea.fimo.shared.error.FimoException

class PostAuthorizationException : FimoException(
    message = "Post authorization failed",
    code = "post.authorization.failed",
    status = 403
)