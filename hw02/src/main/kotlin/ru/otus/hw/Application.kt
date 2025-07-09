package ru.otus.hw

import org.springframework.beans.factory.getBean
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import ru.otus.hw.service.TestRunnerService

@ComponentScan
@PropertySource("application.properties")
@Configuration
open class Application

fun main() {
    val context: ApplicationContext = AnnotationConfigApplicationContext(Application::class.java)
    val testRunnerService = context.getBean<TestRunnerService>()
    testRunnerService.run()
}
