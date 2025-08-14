package ru.otus.hw.service

import org.springframework.stereotype.Service
import ru.otus.hw.dao.QuestionDao
import ru.otus.hw.domain.Student
import ru.otus.hw.domain.TestResult

@Service
class TestServiceImpl(
    private val ioService: LocalizedIOService,
    private val questionDao: QuestionDao,
    private val questionHandler: QuestionHandler
) : TestService {
    override fun executeTestFor(student: Student): TestResult {
        val testResult = TestResult(student)

        val questions = questionDao.findAll()

        ioService.printLine("")
        ioService.printLineLocalized("TestService.answer.the.questions")

        questions.mapIndexed { index, question ->
            val isAnswerValid = questionHandler.handle(question, index + 1)
            testResult.applyAnswer(question, isAnswerValid)
        }

        return testResult
    }
}
