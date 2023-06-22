package com.tianea.fimo.domain.post.error

import com.tianea.fimo.shared.error.FimoException

class PostNotFoundException : FimoException(
    message = "Post not found",
    code = "POST_NOT_FOUND",
    status = 400
)