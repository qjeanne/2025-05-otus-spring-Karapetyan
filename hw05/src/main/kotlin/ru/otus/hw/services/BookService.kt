package ru.otus.hw.services

import ru.otus.hw.models.Book

interface BookService {
    fun findById(id: Long): Book?
    fun findAll(): List<Book>
    fun insert(title: String, authorId: Long, genresIds: Set<Long>): Book
    fun update(id: Long, title: String, authorId: Long, genresIds: Set<Long>): Book
    fun deleteById(id: Long)
}
