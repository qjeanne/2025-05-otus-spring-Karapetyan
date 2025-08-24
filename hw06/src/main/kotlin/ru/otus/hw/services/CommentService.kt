package ru.otus.hw.services

import ru.otus.hw.models.Comment

interface CommentService {
    fun findById(id: Long): Comment?
    fun findByBookId(id: Long): List<Comment>
    fun insert(text: String, bookId: Long): Comment
    fun update(id: Long, text: String, bookId: Long): Comment
    fun deleteById(id: Long)
}
