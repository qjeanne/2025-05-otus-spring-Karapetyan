package ru.otus.hw.repositories

import ru.otus.hw.models.Book

interface BookRepository {
    fun findById(id: Long): Book?
    fun findAll(): List<Book>
    fun save(book: Book): Book
    fun deleteById(id: Long)
}
