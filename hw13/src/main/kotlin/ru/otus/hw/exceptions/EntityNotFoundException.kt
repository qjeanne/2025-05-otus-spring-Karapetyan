package ru.otus.hw.exceptions

sealed class EntityNotFoundException(message: String) : RuntimeException(message) {
    data class BookNotFound(val bookId: Long) : EntityNotFoundException("Book $bookId not found")
    data class AuthorNotFound(val authorId: Long) : EntityNotFoundException("Author $authorId not found")
    data class GenresNotFound(val genresIds: Set<Long>) : EntityNotFoundException(
        "One or all genres with ids $genresIds not found"
    )
    data class CommentNotFound(val commentId: Long) : EntityNotFoundException("Comment $commentId not found")
    data class CommentNotFoundForBook(val commentId: Long, val bookId: Long) : EntityNotFoundException(
        "Comment with id $commentId not found for book with id $bookId"
    )
    data class UserNotFound(val username: String) : EntityNotFoundException("User $username not found")
}

