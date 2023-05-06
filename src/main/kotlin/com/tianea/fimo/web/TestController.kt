package com.tianea.fimo.web

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RequestMapping("/test")
@RestController
class TestController {
    @GetMapping("/user")
    fun user(): String = "user"
}