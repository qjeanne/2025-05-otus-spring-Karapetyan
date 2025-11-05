package ru.otus.hw.repositories

import org.springframework.data.repository.Repository
import ru.otus.hw.models.Genre

interface GenreRepository : Repository<Genre, Long> {
    fun findAll(): List<Genre>
    fun findAllByIdIn(ids: Set<Long>): MutableList<Genre>
}
