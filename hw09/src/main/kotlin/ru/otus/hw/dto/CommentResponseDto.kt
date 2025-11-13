package ru.otus.hw.dto

import ru.otus.hw.models.Comment

data class CommentResponseDto(
    val id: Long,
    val text: String,
    val book: BookResponseDto
) {
    companion object {
        fun fromDomainObject(comment: Comment): CommentResponseDto {
            return CommentResponseDto(
                id = comment.id,
                text = comment.text,
                book = BookResponseDto.fromDomainObject(comment.book)
            )
        }
    }

    fun toDomainObject() = Comment(
        id = id,
        text = text,
        book = book.toDomainObject()
    )
}
