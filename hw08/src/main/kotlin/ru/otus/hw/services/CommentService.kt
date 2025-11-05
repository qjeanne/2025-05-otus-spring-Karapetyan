package ru.otus.hw.services

import ru.otus.hw.models.Comment

interface CommentService {
    fun findById(id: String): Comment?
    fun findByBookId(id: String): List<Comment>
    fun insert(text: String, bookId: String): Comment
    fun update(id: String, text: String, bookId: String): Comment
    fun deleteById(id: String)
}
