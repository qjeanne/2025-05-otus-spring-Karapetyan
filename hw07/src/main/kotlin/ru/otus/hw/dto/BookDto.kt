package ru.otus.hw.dto

data class BookDto(
    val id: Long,
    val title: String,
    val author: AuthorDto,
    val genres: List<GenreDto>
)
