package ru.otus.hw.service

import ru.otus.hw.domain.Question

fun interface QuestionFormatter {
    fun format(questions: List<Question>): String
}
