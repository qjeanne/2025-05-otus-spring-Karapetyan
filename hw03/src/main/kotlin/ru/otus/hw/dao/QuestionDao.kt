package ru.otus.hw.dao

import ru.otus.hw.domain.Question

fun interface QuestionDao {
    fun findAll(): List<Question>
}