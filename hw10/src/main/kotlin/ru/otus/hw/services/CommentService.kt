package ru.otus.hw.services

import ru.otus.hw.dto.CommentResponseDto

interface CommentService {
    fun findById(id: Long): CommentResponseDto
    fun findByBookId(id: Long): List<CommentResponseDto>
    fun insert(text: String, bookId: Long): CommentResponseDto
    fun update(id: Long, text: String, bookId: Long): CommentResponseDto
    fun deleteById(id: Long)
}
