package ru.otus.hw.rest

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RestController
import ru.otus.hw.dto.CommentResponseDto
import ru.otus.hw.services.CommentService

@RestController
class CommentController(
    private val service: CommentService
) {
    @GetMapping("/api/books/{id}/comments")
    fun insertComment(
        @PathVariable id: Long
    ): ResponseEntity<List<CommentResponseDto>> =
        ResponseEntity.ok(
            service.findByBookId(id)
        )

    @PostMapping("/api/books/{id}/comments")
    fun insertComment(
        @PathVariable id: Long,
        text: String
    ): ResponseEntity<Unit> {
        service.insert(text, id)
        return ResponseEntity.noContent().build()
    }

    @PutMapping("/api/books/{bookId}/comments/{id}")
    fun updateComment(
        @PathVariable bookId: Long,
        @PathVariable id: Long,
        text: String
    ): ResponseEntity<Unit> {
        service.update(id, text, bookId)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/api/books/{bookId}/comments/{id}")
    fun deleteComment(
        @PathVariable bookId: Long,
        @PathVariable id: Long
    ): ResponseEntity<Unit> {
        service.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}
