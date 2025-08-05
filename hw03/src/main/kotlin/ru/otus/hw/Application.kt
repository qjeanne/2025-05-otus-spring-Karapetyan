package ru.otus.hw

import org.springframework.beans.factory.getBean
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.ApplicationContext
import ru.otus.hw.config.AppProperties
import ru.otus.hw.service.TestRunnerService

@SpringBootApplication
@EnableConfigurationProperties(AppProperties::class)
open class Application

fun main() {
    val context: ApplicationContext = SpringApplication.run(Application::class.java)
    val testRunnerService = context.getBean<TestRunnerService>()
    testRunnerService.run()
}
