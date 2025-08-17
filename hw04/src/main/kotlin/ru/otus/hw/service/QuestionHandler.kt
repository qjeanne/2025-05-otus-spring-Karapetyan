package ru.otus.hw.service

import ru.otus.hw.domain.Question

fun interface QuestionHandler {
    fun handle(question: Question, questionNumber: Int): Boolean
}
