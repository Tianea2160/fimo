package com.tianea.fimo.shared.provider

import org.springframework.stereotype.Service
import java.util.*


@Service
class IdentifierProvider() {
    fun generateId(): String = UUID.randomUUID().toString()
}