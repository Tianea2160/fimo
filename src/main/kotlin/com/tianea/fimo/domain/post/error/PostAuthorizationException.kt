package com.tianea.fimo.domain.post.error

import com.tianea.fimo.shared.error.FimoException

class PostAuthorizationException : FimoException(
    message = "Post authorization failed",
    code = "POST_AUTHORIZATION_ERROR",
    status = 403
)