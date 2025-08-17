package ru.otus.hw.shell

import org.springframework.shell.command.annotation.Command
import ru.otus.hw.service.TestRunnerService

@Command(group = "Test Commands")
class TestCommand (
    private val testRunnerService: TestRunnerService
) {
    @Command(command = ["start-test"])
    fun startTest() = testRunnerService.run()
}
