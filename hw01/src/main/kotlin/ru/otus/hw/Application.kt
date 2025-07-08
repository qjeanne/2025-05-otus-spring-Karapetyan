package ru.otus.hw

import org.springframework.beans.factory.getBean
import org.springframework.context.ApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext
import ru.otus.hw.service.TestRunnerService

fun main() {
    val context: ApplicationContext = ClassPathXmlApplicationContext("spring-context.xml")
    val testRunnerService = context.getBean<TestRunnerService>()
    testRunnerService.run()
}
