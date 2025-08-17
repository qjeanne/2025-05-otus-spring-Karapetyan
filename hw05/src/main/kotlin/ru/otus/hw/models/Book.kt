package ru.otus.hw.models

data class Book(
    var id: Long,
    val title: String,
    val author: Author,
    var genres: List<Genre>
)
