package ru.otus.hw.services

import reactor.core.publisher.Flux
import ru.otus.hw.dto.AuthorResponseDto

fun interface AuthorService {
    fun findAll(): Flux<AuthorResponseDto>
}
