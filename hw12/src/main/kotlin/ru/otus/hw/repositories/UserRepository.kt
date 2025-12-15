package ru.otus.hw.repositories

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono
import ru.otus.hw.models.User

interface UserRepository : ReactiveMongoRepository<User, String> {
    fun findByUsernameValue(username: String): Mono<User>
}
