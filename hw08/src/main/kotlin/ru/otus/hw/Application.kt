package ru.otus.hw

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.shell.command.annotation.CommandScan

@SpringBootApplication
@CommandScan
open class Application

fun main() {
    SpringApplication.run(Application::class.java)
}