package ru.otus.hw.repositories

import ru.otus.hw.models.Author

interface AuthorRepository {
    fun findAll(): List<Author>
    fun findById(id: Long): Author?
}
