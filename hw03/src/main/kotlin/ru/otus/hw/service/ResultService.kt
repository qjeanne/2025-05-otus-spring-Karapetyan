package ru.otus.hw.service

import ru.otus.hw.domain.TestResult

fun interface ResultService {
    fun showResult(testResult: TestResult)
}
