package ru.otus.hw.repositories

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.repository.Repository
import ru.otus.hw.models.Book

interface BookRepository : Repository<Book, Long> {
    fun findById(id: Long): Book?

    @EntityGraph(attributePaths = ["author"])
    fun findAll(): List<Book>

    fun save(book: Book): Book

    fun deleteById(id: Long)
}
