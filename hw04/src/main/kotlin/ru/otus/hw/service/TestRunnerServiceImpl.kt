package ru.otus.hw.service

import org.springframework.stereotype.Service
import ru.otus.hw.exceptions.QuestionReadException

@Service
class TestRunnerServiceImpl(
    private val testService: TestService,
    private val studentService: StudentService,
    private val resultService: ResultService,
    private val ioService: LocalizedIOService
) : TestRunnerService {
    override fun run() {
        val student = studentService.determineCurrentStudent()

        try {
            val testResult = testService.executeTestFor(student)
            resultService.showResult(testResult)
        } catch (e: QuestionReadException) {
            ioService.printLineLocalized("TestRunnerService.unable.to.load.questions")
        } catch (e: Exception) {
            ioService.printLineLocalized("TestRunnerService.unexpected.error")
        }
    }
}
