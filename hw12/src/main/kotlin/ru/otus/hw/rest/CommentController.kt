package ru.otus.hw.rest

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import ru.otus.hw.dto.CommentResponseDto
import ru.otus.hw.services.CommentService

data class CommentForm(val text: String)

@RestController
class CommentController(
    private val service: CommentService
) {
    @GetMapping("/api/books/{id}/comments")
    fun insertComment(
        @PathVariable id: String
    ): Flux<CommentResponseDto> = service.findByBookId(id)

    @PostMapping("/api/books/{id}/comments")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun insertComment(
        @PathVariable id: String,
        text: CommentForm
    ): Mono<Void> = service.insert(text.text, id).then()

    @PutMapping("/api/books/{bookId}/comments/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updateComment(
        @PathVariable bookId: String,
        @PathVariable id: String,
        text: CommentForm
    ): Mono<Void> = service.update(id, text.text, bookId).then()

    @DeleteMapping("/api/books/{bookId}/comments/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteComment(
        @PathVariable bookId: String,
        @PathVariable id: String
    ): Mono<Void> = service.deleteById(id)
}
