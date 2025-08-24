package ru.otus.hw.repositories

import ru.otus.hw.models.Genre

interface GenreRepository {
    fun findAll(): List<Genre>
    fun findAllByIds(ids: Set<Long>): List<Genre>
}
