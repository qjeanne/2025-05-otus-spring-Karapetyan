package ru.otus.hw.services

import ru.otus.hw.models.Book

interface BookService {
    fun findById(id: String): Book?
    fun findAll(): List<Book>
    fun insert(title: String, authorId: String, genresIds: Set<String>): Book
    fun update(id: String, title: String, authorId: String, genresIds: Set<String>): Book
    fun deleteById(id: String)
}
