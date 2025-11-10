package ru.otus.hw.services

import ru.otus.hw.models.Genre

fun interface GenreService {
    fun findAll(): List<Genre>
}
