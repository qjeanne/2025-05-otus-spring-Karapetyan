package ru.otus.hw.service

import ru.otus.hw.domain.Student
import ru.otus.hw.domain.TestResult


fun interface TestService {
    fun executeTestFor(student: Student): TestResult
}
