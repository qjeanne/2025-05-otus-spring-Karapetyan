package ru.otus.hw.service

import org.springframework.stereotype.Service

@Service
class TestRunnerServiceImpl(
    private val testService: TestService,
    private val studentService: StudentService,
    private val resultService: ResultService
) : TestRunnerService {
    override fun run() {
        val student = studentService.determineCurrentStudent()
        val testResult = testService.executeTestFor(student)

        resultService.showResult(testResult)
    }
}
