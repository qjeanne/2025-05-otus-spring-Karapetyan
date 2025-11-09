package ru.otus.hw.repositories

import org.springframework.data.mongodb.repository.MongoRepository
import ru.otus.hw.models.Comment

interface CommentRepository :
    MongoRepository<Comment, String>,
    CommentRepositoryCustom
{
    fun findByBookId(id: String): MutableList<Comment>
    fun deleteByBookId(id: String)
}
