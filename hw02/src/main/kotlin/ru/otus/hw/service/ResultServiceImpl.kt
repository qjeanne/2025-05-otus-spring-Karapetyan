package ru.otus.hw.service

import org.springframework.stereotype.Service
import ru.otus.hw.config.TestConfig
import ru.otus.hw.domain.TestResult

@Service
class ResultServiceImpl(
    private val ioService: IOService,
    private val testConfig: TestConfig
) : ResultService {
    override fun showResult(testResult: TestResult) {
        ioService.printLine("")
        ioService.printLine("Test results: ")
        ioService.printFormattedLine("Student: %s", testResult.student.getFullName())
        ioService.printFormattedLine("Answered questions count: %d", testResult.answeredQuestions.size)
        ioService.printFormattedLine("Right answers count: %d", testResult.rightAnswersCount)

        if (testResult.rightAnswersCount >= testConfig.rightAnswersCountToPass) {
            ioService.printLine("Congratulations! You passed test!")
            return
        }
        ioService.printLine("Sorry. You fail test.")
    }
}
