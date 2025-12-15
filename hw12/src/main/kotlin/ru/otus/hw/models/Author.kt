package ru.otus.hw.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "authors")
data class Author(
    @Id
    var id: String? = null,

    @Indexed(name = "full_name", unique = true)
    var fullName: String
)
