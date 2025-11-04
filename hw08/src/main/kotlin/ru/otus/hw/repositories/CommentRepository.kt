package ru.otus.hw.repositories

import org.springframework.data.mongodb.repository.MongoRepository
import ru.otus.hw.models.Comment

interface CommentRepository : MongoRepository<Comment, Long> {
    fun findByBookId(id: Long): MutableList<Comment>
    fun deleteByBookId(id: Long)
}
