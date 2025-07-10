package ru.otus.hw.service

import org.springframework.stereotype.Service
import ru.otus.hw.exceptions.QuestionReadException

@Service
class TestRunnerServiceImpl(
    private val testService: TestService,
    private val studentService: StudentService,
    private val resultService: ResultService,
    private val ioService: IOService
) : TestRunnerService {
    override fun run() {
        val student = studentService.determineCurrentStudent()

        try {
            val testResult = testService.executeTestFor(student)
            resultService.showResult(testResult)
        } catch (e: QuestionReadException) {
            ioService.printLine("Unable to load questions")
        } catch (e: Exception) {
            ioService.printLine("Unexpected error")
        }
    }
}
