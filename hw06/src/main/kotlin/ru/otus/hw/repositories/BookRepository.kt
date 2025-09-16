package ru.otus.hw.repositories

import ru.otus.hw.dto.BookDto
import ru.otus.hw.models.Book

interface BookRepository {
    fun findById(id: Long): BookDto?
    fun findAll(): List<BookDto>
    fun save(book: Book): BookDto
    fun deleteById(id: Long)
}
