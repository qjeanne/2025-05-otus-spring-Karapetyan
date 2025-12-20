package ru.otus.hw.services

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.otus.hw.dto.BookResponseDto

interface BookService {
    fun findById(id: String): Mono<BookResponseDto>
    fun findAll(): Flux<BookResponseDto>
    fun insert(title: String, authorId: String, genresIds: Set<String>): Mono<BookResponseDto>
    fun update(id: String, title: String, authorId: String, genresIds: Set<String>): Mono<BookResponseDto>
    fun deleteById(id: String): Mono<Void>
}
