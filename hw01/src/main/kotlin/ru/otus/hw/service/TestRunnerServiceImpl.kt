package ru.otus.hw.service

class TestRunnerServiceImpl(private val testService: TestService) : TestRunnerService {
    override fun run() = testService.executeTest()
}
