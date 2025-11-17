package ru.otus.hw.rest

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import ru.otus.hw.exceptions.EntityNotFoundException
import ru.otus.hw.rest.dto.ErrorResponse

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(EntityNotFoundException::class)
    fun handle(e: EntityNotFoundException): ResponseEntity<ErrorResponse> {
        val message = when (e) {
            is EntityNotFoundException.BookNotFound -> "Book not found"
            is EntityNotFoundException.AuthorNotFound -> "Author not found"
            is EntityNotFoundException.GenresNotFound -> "One or all genres not found"
            is EntityNotFoundException.CommentNotFound -> "Comment not found"
            is EntityNotFoundException.CommentNotFoundForBook -> "This comment belongs to another book"
        }

        return ResponseEntity(
            ErrorResponse(message),
            HttpStatus.NOT_FOUND
        )
    }

    @ExceptionHandler(Exception::class)
    fun handle(e: Exception): ResponseEntity<ErrorResponse> =
        ResponseEntity(
            ErrorResponse("Unexpected error"),
            HttpStatus.UNPROCESSABLE_ENTITY
        )
}
