package ru.otus.hw.repositories

import org.springframework.data.repository.Repository
import ru.otus.hw.models.Comment

interface CommentRepository : Repository<Comment, Long> {
    fun findById(id: Long): Comment?
    fun findByBookId(id: Long): List<Comment>
    fun save(comment: Comment): Comment
    fun deleteById(id: Long)
}