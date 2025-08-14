package ru.otus.hw.service

import org.springframework.stereotype.Service
import ru.otus.hw.config.TestConfig
import ru.otus.hw.domain.TestResult

@Service
class ResultServiceImpl(
    private val ioService: LocalizedIOService,
    private val testConfig: TestConfig
) : ResultService {
    override fun showResult(testResult: TestResult) {
        ioService.printLine("")
        ioService.printLineLocalized("ResultService.test.results")
        ioService.printFormattedLineLocalized("ResultService.student", testResult.student.getFullName())
        ioService.printFormattedLineLocalized("ResultService.answered.questions.count", testResult.answeredQuestions.size)
        ioService.printFormattedLineLocalized("ResultService.right.answers.count", testResult.rightAnswersCount)

        if (testResult.rightAnswersCount >= testConfig.rightAnswersCountToPass) {
            ioService.printLineLocalized("ResultService.passed.test")
            return
        }
        ioService.printLineLocalized("ResultService.fail.test")
    }
}
