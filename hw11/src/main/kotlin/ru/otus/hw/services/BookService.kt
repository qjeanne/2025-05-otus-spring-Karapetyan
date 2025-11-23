package ru.otus.hw.services

import ru.otus.hw.dto.BookResponseDto

interface BookService {
    fun findById(id: Long): BookResponseDto
    fun findAll(): List<BookResponseDto>
    fun insert(title: String, authorId: Long, genresIds: Set<Long>): BookResponseDto
    fun update(id: Long, title: String, authorId: Long, genresIds: Set<Long>): BookResponseDto
    fun deleteById(id: Long)
}
