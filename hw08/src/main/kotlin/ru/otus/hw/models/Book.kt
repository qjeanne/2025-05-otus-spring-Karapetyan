package ru.otus.hw.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "books")
data class Book(
    @Id
    var id: Long,

    var title: String,

    var author: Author,

    var genres: MutableList<Genre> = mutableListOf()
)
