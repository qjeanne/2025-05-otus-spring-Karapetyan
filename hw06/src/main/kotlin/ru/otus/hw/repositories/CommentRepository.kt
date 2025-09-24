package ru.otus.hw.repositories

import ru.otus.hw.models.Comment

interface CommentRepository {
    fun findById(id: Long): Comment?
    fun findByBookId(id: Long): List<Comment>
    fun save(comment: Comment): Comment
    fun deleteById(id: Long)
}