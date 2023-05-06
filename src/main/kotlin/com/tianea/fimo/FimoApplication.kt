package com.tianea.fimo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FimoApplication

fun main(args: Array<String>) {
    runApplication<FimoApplication>(*args)
}
