package ru.otus.hw.service

import org.springframework.stereotype.Service
import ru.otus.hw.dao.QuestionDao
import ru.otus.hw.domain.Student
import ru.otus.hw.domain.TestResult
import ru.otus.hw.exceptions.QuestionReadException

@Service
class TestServiceImpl(
    private val ioService: IOService,
    private val questionDao: QuestionDao,
    private val questionHandler: QuestionHandler
) : TestService {
    override fun executeTestFor(student: Student): TestResult {
        val testResult = TestResult(student)

        val questions = try {
            questionDao.findAll()
        } catch (e: QuestionReadException) {
            ioService.printLine("Unable to load questions")
            return testResult
        } catch (e: Exception) {
            ioService.printLine("Unexpected error")
            return testResult
        }

        ioService.printLine("")
        ioService.printFormattedLine("Please answer the questions below%n")

        questions.mapIndexed { index, question ->
            val isAnswerValid = questionHandler.handle(question, index + 1)
            testResult.applyAnswer(question, isAnswerValid)
        }

        return testResult
    }
}
