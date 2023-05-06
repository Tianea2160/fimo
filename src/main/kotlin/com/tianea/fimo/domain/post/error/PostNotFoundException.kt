package com.tianea.fimo.domain.post.error

import com.tianea.fimo.shared.error.FimoException

class PostNotFoundException : FimoException(
    message = "Post not found",
    code = "post.not.found",
    status = 404
)