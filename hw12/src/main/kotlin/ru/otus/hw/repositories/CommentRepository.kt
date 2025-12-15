package ru.otus.hw.repositories

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import ru.otus.hw.models.Comment

interface CommentRepository :
    ReactiveMongoRepository<Comment, String>,
    CommentRepositoryCustom {
    fun findByBookId(id: String): Flux<Comment>
}
