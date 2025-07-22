package ru.otus.hw.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
data class AppProperties(
    @Value("\${test.rightAnswersCountToPass}")
    override val rightAnswersCountToPass: Int,

    @Value("\${test.fileName}")
    override val testFileName: String
) : TestConfig, TestFileNameProvider
