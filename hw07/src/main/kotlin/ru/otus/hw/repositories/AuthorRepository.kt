package ru.otus.hw.repositories

import org.springframework.data.repository.Repository
import ru.otus.hw.models.Author

interface AuthorRepository : Repository<Author, Long> {
    fun findAll(): List<Author>
    fun findById(id: Long): Author?
}
