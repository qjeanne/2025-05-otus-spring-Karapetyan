package ru.otus.hw.services

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.otus.hw.dto.CommentResponseDto

interface CommentService {
    fun findById(id: String): Mono<CommentResponseDto>
    fun findByBookId(id: String): Flux<CommentResponseDto>
    fun insert(text: String, bookId: String): Mono<CommentResponseDto>
    fun update(id: String, text: String, bookId: String): Mono<CommentResponseDto>
    fun deleteById(id: String): Mono<Void>
}
