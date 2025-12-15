package ru.otus.hw.repositories

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.otus.hw.models.Genre

interface GenreRepository : ReactiveMongoRepository<Genre, String> {
    fun findAllByIdIn(ids: Set<String>): Flux<Genre>
}
