package ru.otus.hw.dto

data class BookRequestDto(
    val title: String,
    val authorId: Long,
    val genresIds: Set<Long>
)
