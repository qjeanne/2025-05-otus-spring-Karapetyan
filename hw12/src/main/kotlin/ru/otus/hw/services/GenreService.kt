package ru.otus.hw.services

import reactor.core.publisher.Flux
import ru.otus.hw.dto.GenreResponseDto

fun interface GenreService {
    fun findAll(): Flux<GenreResponseDto>
}
