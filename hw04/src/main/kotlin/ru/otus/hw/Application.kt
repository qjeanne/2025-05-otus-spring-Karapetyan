package ru.otus.hw

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.shell.command.annotation.CommandScan
import ru.otus.hw.config.AppProperties


@SpringBootApplication
@EnableConfigurationProperties(AppProperties::class)
@CommandScan
open class Application

fun main() {
    SpringApplication.run(Application::class.java)
}
