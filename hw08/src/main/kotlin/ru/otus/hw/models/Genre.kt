package ru.otus.hw.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "genres")
data class Genre(
    @Id
    var id: Long,

    @Indexed(name = "name", unique = true)
    val name: String
)
