package ru.otus.hw.dto

data class BookRequestDto(
    val title: String,
    val authorId: String,
    val genresIds: Set<String>
)
