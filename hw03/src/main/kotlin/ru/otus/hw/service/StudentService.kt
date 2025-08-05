package ru.otus.hw.service

import ru.otus.hw.domain.Student

fun interface StudentService {
    fun determineCurrentStudent(): Student
}
