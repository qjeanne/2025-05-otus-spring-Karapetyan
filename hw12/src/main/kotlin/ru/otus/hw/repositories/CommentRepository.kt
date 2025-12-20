package ru.otus.hw.repositories

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.otus.hw.models.Comment

interface CommentRepository :
    ReactiveMongoRepository<Comment, String>,
    CommentRepositoryCustom {
    fun findByBookId(id: String): Flux<Comment>
    fun deleteAllByBookId(id: String): Mono<Void>
}
